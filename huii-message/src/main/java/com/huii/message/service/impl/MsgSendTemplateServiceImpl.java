package com.huii.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.message.core.service.MailService;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.message.domain.MsgSendLog;
import com.huii.message.domain.MsgSendTemplate;
import com.huii.message.domain.MsgSubscribeUser;
import com.huii.message.enums.MsgSendType;
import com.huii.message.exception.MailException;
import com.huii.message.exception.SmsException;
import com.huii.message.mapper.MsgSendLogMapper;
import com.huii.message.mapper.MsgSendTemplateMapper;
import com.huii.message.mapper.MsgSubscribeUserMapper;
import com.huii.message.service.MsgSendTemplateService;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MsgSendTemplateServiceImpl extends ServiceImpl<MsgSendTemplateMapper, MsgSendTemplate> implements MsgSendTemplateService {

    private final MsgSendTemplateMapper msgSendTemplateMapper;
    private final SysUserMapper sysUserMapper;
    private final AliyunSmsServiceImpl smsService;
    private final MailService mailService;
    private final MsgSendLogMapper msgSendLogMapper;
    private final MsgSubscribeUserMapper msgSubscribeUserMapper;

    @Override
    public Page selectMsgSendTemplateList(MsgSendTemplate msgSendTemplate, PageParam pageParam) {
        IPage<MsgSendTemplate> iPage = new PageParamUtils<MsgSendTemplate>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(msgSendTemplate)));
    }

    @Override
    public MsgSendTemplate selectMsgSendTemplateById(Long id) {
        return msgSendTemplateMapper.selectById(id);
    }

    @Override
    public void runMessageSend(MsgSendTemplate msgSendTemplate) {
        String sendType = msgSendTemplate.getSendType();
        if (sendType.equals(MsgSendType.SMS.getValue())) {
            try {
                smsService.send(
                        parseTarget(MsgSendType.SMS, msgSendTemplate.getSendTargets(), msgSendTemplate.getSubId()),
                        msgSendTemplate.getSendTempName(),
                        parseParams(msgSendTemplate.getSendTempParams())
                );
                saveLog(msgSendTemplate, null);

            } catch (Exception e) {
                if (e instanceof ServiceException) {
                    saveLog(msgSendTemplate, ((ServiceException) e).getErrorMsg());
                    throw new SmsException(((ServiceException) e).getErrorMsg());
                }
                saveLog(msgSendTemplate, "短信发送失败");
                throw new SmsException("短信发送失败");
            }
        } else if (sendType.equals(MsgSendType.MAIL.getValue())) {
            try {
                mailService.send(
                        msgSendTemplate.getSendTempName(),
                        parseParams(msgSendTemplate.getSendTempParams()),
                        parseTarget(MsgSendType.MAIL, msgSendTemplate.getSendTargets(), msgSendTemplate.getSubId()).split(",")
                );
                saveLog(msgSendTemplate, null);

            } catch (Exception e) {
                if (e instanceof ServiceException) {
                    saveLog(msgSendTemplate, ((ServiceException) e).getErrorMsg());
                    throw new MailException(((ServiceException) e).getErrorMsg());
                }
                saveLog(msgSendTemplate, "邮件发送失败");
                throw new MailException("邮件发送失败");
            }
        }
    }

    @Override
    public void checkInsert(MsgSendTemplate msgSendTemplate) {
        if (msgSendTemplateMapper.exists(new LambdaQueryWrapper<MsgSendTemplate>()
                .eq(MsgSendTemplate::getTempName, msgSendTemplate.getTempName()))) {
            throw new RuntimeException("模板名称重复");
        }
    }

    @Override
    public void insertMsgSendTemplate(MsgSendTemplate msgSendTemplate) {
        msgSendTemplateMapper.insert(msgSendTemplate);
    }

    @Override
    public void checkUpdate(MsgSendTemplate msgSendTemplate) {
        MsgSendTemplate oldOne = msgSendTemplateMapper.selectById(msgSendTemplate.getTempId());
        if (!StringUtils.equals(msgSendTemplate.getTempName(), oldOne.getTempName())) {
            if (msgSendTemplateMapper.exists(new LambdaQueryWrapper<MsgSendTemplate>()
                    .eq(MsgSendTemplate::getTempName, msgSendTemplate.getTempName()))) {
                throw new RuntimeException("模板名称重复");
            }
        }
    }

    @Override
    public void updateMsgSendTemplate(MsgSendTemplate msgSendTemplate) {
        msgSendTemplateMapper.updateById(msgSendTemplate);
    }

    @Override
    public void deleteMsgSendTemplate(Long[] ids) {
        msgSendTemplateMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<MsgSendTemplate> wrapperBuilder(MsgSendTemplate msgSendTemplate) {
        Map<String, Object> params = msgSendTemplate.getParams();
        LambdaQueryWrapper<MsgSendTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ObjectUtils.isNotEmpty(msgSendTemplate.getTempName()), MsgSendTemplate::getTempName, msgSendTemplate.getTempName())
                .eq(ObjectUtils.isNotEmpty(msgSendTemplate.getSendType()), MsgSendTemplate::getSendType, msgSendTemplate.getSendType())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        MsgSendTemplate::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(MsgSendTemplate::getCreateTime);
        return wrapper;
    }

    /**
     * 解析参数
     */
    private Map<String, String> parseParams(String params) {
        Map<String, String> paramMap = new HashMap<>();

        if (params != null && !params.isEmpty()) {
            String[] keyValuePairs = params.split(",");
            for (String pair : keyValuePairs) {
                String[] entry = pair.split("=");
                if (entry.length == 2) {
                    String key = entry[0].trim();
                    String value = entry[1].trim();
                    paramMap.put(key, value);
                } else {
                    throw new ServiceException("参数解析失败");
                }
            }
        }
        return paramMap;
    }

    /**
     * 解析对象
     */
    private String parseTarget(MsgSendType type, String targets, Long subId) {
        if (StringUtils.isEmpty(targets) && ObjectUtils.isEmpty(subId)) {
            throw new ServiceException("发送对象不能为空");
        }
        if (ObjectUtils.isNotEmpty(subId)) {
            return getSubUsers(type, subId);
        } else {
            return getTargets(type, targets);
        }
    }

    /**
     * 解析targets字段
     */
    @SuppressWarnings("all")
    private String getTargets(MsgSendType type, String targets) {
        try {
            List<Long> idList;
            if (targets.equals("0")) {
                //全选
                idList = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().select(SysUser::getUserId))
                        .stream().map(SysUser::getUserId).toList();
            } else if (targets.contains("-")) {
                //范围发送
                String[] split = targets.split("-");

                idList = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>()
                                .between(SysUser::getUserId, Long.valueOf(split[0]), Long.valueOf(split[1]))
                                .select(SysUser::getUserId))
                        .stream().map(SysUser::getUserId).toList();
            } else {
                //自选ID
                idList = Arrays.stream(targets.split(",")).map(Long::parseLong).toList();
            }

            List<String> results = sysUserMapper.selectTargetUser(idList,
                    MsgSendType.SMS.equals(type) ? MsgSendType.SMS.getValue() : MsgSendType.MAIL.getValue());
            return String.join(",", results);
        } catch (Exception e) {
            throw new ServiceException("对象解析失败");
        }
    }

    /**
     * 解析subId字段
     */
    private String getSubUsers(MsgSendType type, Long subId) {
        List<Long> userIds = msgSubscribeUserMapper.selectList(new LambdaQueryWrapper<MsgSubscribeUser>()
                        .eq(MsgSubscribeUser::getSubId, subId))
                .stream().map(MsgSubscribeUser::getUserId).toList();
        if (ObjectUtils.isEmpty(userIds)) {
            throw new ServiceException("该订阅分组内无用户");
        }
        try {
            List<String> results = sysUserMapper.selectTargetUser(userIds,
                    MsgSendType.SMS.equals(type) ? MsgSendType.SMS.getValue() : MsgSendType.MAIL.getValue());
            return String.join(",", results);
        } catch (Exception e) {
            throw new ServiceException("对象解析失败");
        }
    }

    /**
     * 保存日志
     */
    private void saveLog(MsgSendTemplate msgSendTemplate, String errMsg) {
        MsgSendLog msgSendLog = new MsgSendLog();
        String sendTarget;
        if (ObjectUtils.isNotEmpty(msgSendTemplate.getSubId())) {
            sendTarget = "订阅组：" + msgSendTemplate.getSubId();
        } else {
            if (msgSendTemplate.getSendTargets().equals("0")) {
                sendTarget = "全体用户";
            } else if (msgSendTemplate.getSendTargets().contains("-")) {
                sendTarget = "用户范围：";
            } else {
                sendTarget = "用户ID：";
            }
        }
        msgSendLog.setSendTarget(sendTarget);
        msgSendLog.setTempName(msgSendTemplate.getTempName());
        msgSendLog.setSendType(MsgSendType.getNameByValue(msgSendTemplate.getSendType()));
        msgSendLog.setSendTime(LocalDateTime.now());
        if (StringUtils.isEmpty(errMsg)) {
            msgSendLog.setSendStatus(SystemConstants.STATUS_1);
        } else {
            msgSendLog.setSendStatus(SystemConstants.STATUS_0);
            msgSendLog.setErrInfo(errMsg);
        }
        msgSendLogMapper.insert(msgSendLog);
    }
}
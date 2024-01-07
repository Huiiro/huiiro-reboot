package com.huii.message.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.message.core.service.MailService;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.message.domain.MsgSendTemplate;
import com.huii.message.enums.MsgSendStatus;
import com.huii.message.enums.MsgSendType;
import com.huii.message.exception.MailException;
import com.huii.message.exception.SmsException;
import com.huii.message.mapper.MsgSendTemplateMapper;
import com.huii.message.service.MsgSendTemplateService;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送模板服务层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgSendTemplateServiceImpl extends ServiceImpl<MsgSendTemplateMapper, MsgSendTemplate> implements MsgSendTemplateService {

    private final MsgSendTemplateMapper msgSendTemplateMapper;
    private final SysUserMapper sysUserMapper;
    private final AliyunSmsServiceImpl smsService;
    private final MailService mailService;

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
    @Transactional(rollbackFor = Exception.class)
    public void runMessageSend(MsgSendTemplate msgSendTemplate) {
        String sendStatus = msgSendTemplate.getSendStatus();
        if (sendStatus.equals(MsgSendStatus.SEND_SUC.getValue())) {
            throw new ServiceException("该信息已成功发送");
        } else {
            String sendType = msgSendTemplate.getSendType();
            if (sendType.equals(MsgSendType.SMS.getValue())) {
                try {
                    smsService.send(parseTarget(MsgSendType.SMS, msgSendTemplate.getSendTargets()),
                            msgSendTemplate.getTempName(),
                            parseParams(msgSendTemplate.getTempParams())
                    );

                    msgSendTemplate.setSendTime(LocalDateTime.now());
                    msgSendTemplate.setSendStatus(MsgSendStatus.SEND_SUC.getValue());
                    msgSendTemplateMapper.updateById(msgSendTemplate);
                } catch (Exception e) {
                    if (e instanceof ServiceException) {
                        throw new SmsException(((ServiceException) e).getErrorMsg());
                    }
                    throw new SmsException("短信发送失败");
                }
            } else if (sendType.equals(MsgSendType.MAIL.getValue())) {
                try {
                    mailService.send(msgSendTemplate.getTempName(),
                            parseParams(msgSendTemplate.getTempParams()),
                            parseTarget(MsgSendType.MAIL, msgSendTemplate.getSendTargets()).split(",")
                    );

                    msgSendTemplate.setSendTime(LocalDateTime.now());
                    msgSendTemplate.setSendStatus(MsgSendStatus.SEND_SUC.getValue());
                    msgSendTemplateMapper.updateById(msgSendTemplate);
                } catch (Exception e) {
                    if (e instanceof ServiceException) {
                        throw new MailException(((ServiceException) e).getErrorMsg());
                    }
                    throw new MailException("邮件发送失败");
                }
            }
        }
    }

    @Override
    public void checkInsert(MsgSendTemplate msgSendTemplate) {
    }

    @Override
    public void insertMsgSendTemplate(MsgSendTemplate msgSendTemplate) {
        msgSendTemplate.setSendStatus(MsgSendStatus.TO_SEND.getValue());
        msgSendTemplateMapper.insert(msgSendTemplate);
    }

    @Override
    public void checkUpdate(MsgSendTemplate msgSendTemplate) {
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
                .eq(ObjectUtils.isNotEmpty(msgSendTemplate.getSendStatus()), MsgSendTemplate::getSendStatus, msgSendTemplate.getSendStatus())
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
    @SuppressWarnings("all")
    private String parseTarget(MsgSendType type, String targets) {
        try {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            if (targets.equals("0")) {
                //全选
            } else if (targets.contains("-")) {
                //范围发送
                String[] split = targets.split("-");
                wrapper.between(SysUser::getUserId, Long.valueOf(split[0]), Long.valueOf(split[1]));
            } else {
                List<Long> targetList = Arrays.stream(targets.split(","))
                        .map(Long::parseLong)
                        .toList();
                wrapper.in(SysUser::getUserId, targetList);
            }
            wrapper.select(MsgSendType.SMS.equals(type), SysUser::getPhone);
            wrapper.select(MsgSendType.MAIL.equals(type), SysUser::getEmail);
            List<String> results = sysUserMapper.selectList(wrapper).stream().map(i -> {
                if (MsgSendType.SMS.equals(type)) {
                    return i.getPhone();
                } else if (MsgSendType.MAIL.equals(type)) {
                    return i.getEmail();
                }
                return null;
            }).toList();
            StrBuilder sb = new StrBuilder();
            sb.append(String.join(",", results));
            return sb.toString();
        } catch (Exception e) {
            throw new ServiceException("对象解析失败");
        }
    }
}
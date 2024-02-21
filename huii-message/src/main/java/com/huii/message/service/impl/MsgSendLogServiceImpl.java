package com.huii.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.message.domain.MsgSendLog;
import com.huii.message.mapper.MsgSendLogMapper;
import com.huii.message.service.MsgSendLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MsgSendLogServiceImpl extends ServiceImpl<MsgSendLogMapper, MsgSendLog> implements MsgSendLogService {

    private final MsgSendLogMapper msgSendLogMapper;

    @Override
    public Page selectMsgSendLogList(MsgSendLog msgSendLog, PageParam pageParam) {
        IPage<MsgSendLog> iPage = new PageParamUtils<MsgSendLog>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(msgSendLog)));
    }

    @Override
    public List<MsgSendLog> selectMsgSendLogList(MsgSendLog msgSendLog) {
        return msgSendLogMapper.selectList(wrapperBuilder(msgSendLog));
    }

    @Override
    public void deleteMsgSendLog(Long[] ids) {
        msgSendLogMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void deleteMsgSendLogAll() {
        msgSendLogMapper.delete(null);
    }


    private LambdaQueryWrapper<MsgSendLog> wrapperBuilder(MsgSendLog msgSendLog) {
        Map<String, Object> params = msgSendLog.getParams();
        LambdaQueryWrapper<MsgSendLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(msgSendLog.getSendType()), MsgSendLog::getSendType, msgSendLog.getSendType())
                .eq(ObjectUtils.isNotEmpty(msgSendLog.getSendStatus()), MsgSendLog::getSendStatus, msgSendLog.getSendStatus())
                .like(ObjectUtils.isNotEmpty(msgSendLog.getTempName()), MsgSendLog::getTempName, msgSendLog.getTempName())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        MsgSendLog::getSendTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")));
        return wrapper;
    }
}

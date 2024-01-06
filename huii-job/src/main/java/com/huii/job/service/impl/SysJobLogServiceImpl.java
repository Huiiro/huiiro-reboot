package com.huii.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.job.domain.SysJobLog;
import com.huii.job.mapper.SysJobLogMapper;
import com.huii.job.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 任务日志服务层实现
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Service
@RequiredArgsConstructor
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements SysJobLogService {

    private final SysJobLogMapper sysJobLogMapper;

    @Override
    public Page selectSysJobLogList(SysJobLog sysJobLog, PageParam pageParam) {
        IPage<SysJobLog> iPage = new PageParamUtils<SysJobLog>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysJobLog)));
    }

    @Override
    public List<SysJobLog> selectSysJobLogList(SysJobLog sysJobLog) {
        return sysJobLogMapper.selectList(wrapperBuilder(sysJobLog));
    }

    @Override
    public SysJobLog selectSysJobLogById(Long id) {
        return sysJobLogMapper.selectById(id);
    }

    @Override
    public void insertSysJobLog(SysJobLog sysJobLog) {
        sysJobLogMapper.insert(sysJobLog);
    }

    @Override
    public void deleteSysJobLog(Long[] ids) {
        sysJobLogMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void deleteSysJobLogAll() {
        sysJobLogMapper.delete(null);
    }

    private LambdaQueryWrapper<SysJobLog> wrapperBuilder(SysJobLog sysJobLog) {
        Map<String, Object> params = sysJobLog.getParams();
        LambdaQueryWrapper<SysJobLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(sysJobLog.getLogId()), SysJobLog::getLogId, sysJobLog.getLogId())
                .like(ObjectUtils.isNotEmpty(sysJobLog.getJobName()), SysJobLog::getJobName, sysJobLog.getJobName())
                .like(ObjectUtils.isNotEmpty(sysJobLog.getGroupName()), SysJobLog::getGroupName, sysJobLog.getGroupName())
                .eq(ObjectUtils.isNotEmpty(sysJobLog.getJobStatus()), SysJobLog::getJobStatus, sysJobLog.getJobStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysJobLog::getBeginTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")));
        return wrapper;
    }
}
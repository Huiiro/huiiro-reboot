package com.huii.job.mapper;

import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.job.domain.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务日志数据层接口
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Mapper
public interface SysJobLogMapper extends BaseMapperPlus<SysJobLog> {
}
package com.huii.system.mapper;

import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDicDataMapper extends BaseMapperPlus<SysDicData> {
    List<SysDicData> selectByType(String type);
}

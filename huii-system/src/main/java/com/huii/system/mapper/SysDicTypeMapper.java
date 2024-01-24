package com.huii.system.mapper;

import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysDicTypeMapper extends BaseMapperPlus<SysDicType> {

    /**
     * 根据type名称查询type信息
     *
     * @param typeName typeName
     * @return sysDicType
     */
    SysDicType selectByDicType(String typeName);
}

package com.huii.generator.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GenTableMapper extends BaseMapperPlus<GenTable> {
    Page<GenTable> loadDbData(@Param("page") Page<GenTable> page, @Param("genTable") GenTable genTable);

    List<GenTable> loadDbDataGroup();

    List<GenColumn> selectColumnByTableName(String table);
}

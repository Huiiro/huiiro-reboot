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

    /**
     * 查询数据库中的表
     * @param page page
     * @param genTable genTable
     * @return List<GenTable>
     */
    Page<GenTable> loadDbData(@Param("page") Page<GenTable> page, @Param("genTable") GenTable genTable);

    /**
     * 查询数据库中的表分组
     * not command, using loadDbData(Page<GenTable> page, GenTable genTable) instead
     * @return List<GenTable>
     */
    List<GenTable> loadDbDataGroup();


    /**
     * 根据表明查询列
     * @param table tableName
     * @return columns
     */
    List<GenColumn> selectColumnByTableName(String table);

    /**
     * 查询table和columns信息
     * @param id tableId
     * @return table
     */
    GenTable selectTableAndColumns(Long id);
}

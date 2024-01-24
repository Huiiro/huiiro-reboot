package com.huii.generator.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface GenTableService {

    /**
     * 从db读取表数据
     *
     * @param genTable  genTable
     * @param pageParam pageParam
     * @return page
     */
    Page loadDbData(GenTable genTable, PageParam pageParam);

    /**
     * 查询生成表分页
     *
     * @param genTable  genTable
     * @param pageParam pageParam
     * @return page
     */
    Page selectTableList(GenTable genTable, PageParam pageParam);

    /**
     * 查询生成表
     *
     * @param id id
     * @return genTable
     */
    GenTable selectOne(Long id);

    /**
     * 通过表名查询生成列信息
     *
     * @param tableName tableName
     * @return list
     */
    List<GenColumn> selectColumnsByTableName(String tableName);

    /**
     * 查询生成列集合
     *
     * @param ids ids
     * @return list
     */
    List<GenTable> selectBatchByIds(Long[] ids);

    /**
     * 插入生成表
     *
     * @param tables tables
     */
    void insertTable(String[] tables);

    /**
     * 修改生成表
     *
     * @param genTable genTable
     */
    void updateTable(GenTable genTable);

    /**
     * 删除生成表
     *
     * @param ids ids
     */
    void deleteTable(Long[] ids);

    /**
     * 生成表
     *
     * @param list     list
     * @param response response
     */
    void genCode(List<GenTable> list, HttpServletResponse response);

}

package com.huii.generator.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface GenTableService {
    Page loadDbData(GenTable genTable, PageParam pageParam);

    Page selectTableList(GenTable genTable, PageParam pageParam);

    GenTable selectOne(Long id);

    List<GenColumn> selectColumnsByTableName(String tableName);

    List<GenTable> selectBatchByIds(Long[] ids);

    void insertTable(String[] tables);

    void updateTable(GenTable genTable);

    void deleteTable(Long[] ids);

    void genCode(List<GenTable> list, HttpServletResponse response);

}

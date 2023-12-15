package com.huii.generator.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.entity.GenTable;

import java.util.List;

public interface GenTableService {
    Page loadDbData(GenTable genTable, PageParam pageParam);

    Page selectTableList(GenTable genTable, PageParam pageParam);

    GenTable selectOne(Long id);

    List<GenColumn> selectColumnsByTableName(String tableName);

    void insertTable(String[] tables);

    void updateTable(GenTable genTable);

    void deleteTable(Long[] ids);
}

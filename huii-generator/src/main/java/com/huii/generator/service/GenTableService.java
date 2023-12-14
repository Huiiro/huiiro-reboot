package com.huii.generator.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.generator.entity.GenTable;

public interface GenTableService {
    Page loadDbData(GenTable genTable, PageParam pageParam);

    Page selectTableList(GenTable genTable, PageParam pageParam);

    GenTable selectOne(Long id);

    void updateTable(GenTable genTable);

    void deleteTable(Long[] ids);
}

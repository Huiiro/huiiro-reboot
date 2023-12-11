package com.huii.system.service;

import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

public interface SysDicTypeService {
    void clearCache();

    void load();

    void refreshCache();

    Page selectTypeList(SysDicType sysDicType, PageParam pageParam);

    SysDicType selectDicTypeById(Long id);

    SysDicType selectDicTypeByDicType(String dicType);

    void checkInsertDicType(SysDicType sysDicType);

    void insertDicType(SysDicType sysDicType);

    void checkUpdateDicType(SysDicType sysDicType);

    void updateDicType(SysDicType sysDicType);

    void deleteDicType(Long[] ids);
}

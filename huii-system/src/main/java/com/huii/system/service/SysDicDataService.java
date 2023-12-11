package com.huii.system.service;

import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

public interface SysDicDataService {
    Page selectDataList(SysDicData sysDicData, PageParam pageParam);

    SysDicData selectDicDataById(Long id);

    void checkInsertDicData(SysDicData sysDicData);

    void insertDicData(SysDicData sysDicData);

    void checkUpdateDicData(SysDicData sysDicData);

    void updateDicData(SysDicData sysDicData);

    void deleteDicData(Long[] ids);
}

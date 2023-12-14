package com.huii.system.service;

import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysDicDataService {
    Page selectDataList(SysDicData sysDicData, PageParam pageParam);

    List<SysDicData> selectDataList(SysDicData sysDicData);

    SysDicData selectDicDataById(Long id);

    void checkInsertDicData(SysDicData sysDicData);

    void insertDicData(SysDicData sysDicData);

    void checkUpdateDicData(SysDicData sysDicData);

    void updateDicData(SysDicData sysDicData);

    void deleteDicData(Long[] ids);
}

package com.huii.system.service;

import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysDicDataService {

    /**
     * 查询字典数据集合
     *
     * @param sysDicData sysDicData
     * @param pageParam  pageParam
     * @return page
     */
    Page selectDataList(SysDicData sysDicData, PageParam pageParam);

    /**
     * 查询字典数据集合
     *
     * @param sysDicData sysDicData
     * @return list
     */
    List<SysDicData> selectDataList(SysDicData sysDicData);

    /**
     * 查询字典数据
     *
     * @param id id
     * @return sysDicData
     */
    SysDicData selectDicDataById(Long id);

    /**
     * 校验插入数据
     *
     * @param sysDicData sysDicData
     */
    void checkInsertDicData(SysDicData sysDicData);

    /**
     * 添加字典数据
     *
     * @param sysDicData sysDicData
     */
    void insertDicData(SysDicData sysDicData);

    /**
     * 校验更新数据
     *
     * @param sysDicData sysDicData
     */
    void checkUpdateDicData(SysDicData sysDicData);

    /**
     * 更新字典数据
     *
     * @param sysDicData sysDicData
     */
    void updateDicData(SysDicData sysDicData);

    /**
     * 删除字典数据
     *
     * @param ids ids
     */
    void deleteDicData(Long[] ids);
}

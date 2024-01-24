package com.huii.system.service;

import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysDicTypeService {

    /**
     * 清除缓存
     */
    void clearCache();

    /**
     * 加载字典
     */
    void load();

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 查询字典类型集合分页
     *
     * @param sysDicType sysDicType
     * @param pageParam  pageParam
     * @return page
     */
    Page selectTypeList(SysDicType sysDicType, PageParam pageParam);

    /**
     * 查询字典类型集合
     *
     * @param sysDicType sysDicType
     * @return list
     */
    List<SysDicType> selectTypeList(SysDicType sysDicType);

    /**
     * 查询字典类型集合标签
     *
     * @param list list
     * @return list label
     */
    List<Label> buildTypeLabel(List<SysDicType> list);

    /**
     * 查询字典类型
     *
     * @param id id
     * @return sysDicType
     */
    SysDicType selectDicTypeById(Long id);

    /**
     * 查询字典类型
     *
     * @param dicType dicType
     * @return sysDicType
     */
    SysDicType selectDicTypeByDicType(String dicType);

    /**
     * 检查插入数据
     *
     * @param sysDicType sysDicType
     */
    void checkInsertDicType(SysDicType sysDicType);

    /**
     * 插入字典类型
     *
     * @param sysDicType sysDicType
     */
    void insertDicType(SysDicType sysDicType);

    /**
     * 更新插入数据
     *
     * @param sysDicType sysDicType
     */
    void checkUpdateDicType(SysDicType sysDicType);

    /**
     * 更新字典类型
     *
     * @param sysDicType sysDicType
     */
    void updateDicType(SysDicType sysDicType);

    /**
     * 删除字典类型
     *
     * @param ids ids
     */
    void deleteDicType(Long[] ids);
}

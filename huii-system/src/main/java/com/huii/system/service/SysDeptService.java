package com.huii.system.service;

import com.huii.common.core.domain.SysDept;
import com.huii.common.core.model.Tree;

import java.util.List;

public interface SysDeptService {

    /**
     * 获取部门列表
     *
     * @param sysDept sysDept
     * @return list
     */
    List<SysDept> selectDeptList(SysDept sysDept);

    /**
     * 获取部门树表
     *
     * @param dept dept
     * @return List<SysDept>
     */
    List<SysDept> buildTree(List<SysDept> dept);

    /**
     * 获取部门下拉框选项
     *
     * @param dept        dept
     * @param addHeadNode addHeadNode
     * @return List<Tree>
     */
    List<Tree> buildSelect(List<SysDept> dept, Boolean addHeadNode);

    /**
     * 构建分配数据权限时的下拉框选项
     *
     * @param roleId roleId
     * @return List<Long>
     */
    List<Long> selectDeptIdsByRoleId(Long roleId);

    /**
     * 获取部门
     *
     * @param id id
     * @return sysDept
     */
    SysDept selectDeptById(Long id);

    /**
     * 校验添加部门数据
     *
     * @param sysDept sysDept
     */
    void checkInsert(SysDept sysDept);

    /**
     * 添加部门
     *
     * @param sysDept sysDept
     */
    void insertDept(SysDept sysDept);

    /**
     * 校验更新部门数据
     *
     * @param sysDept sysDept
     */
    void checkUpdate(SysDept sysDept);

    /**
     * 更新部门
     *
     * @param sysDept sysDept
     */
    void updateDept(SysDept sysDept);

    /**
     * 删除部门
     *
     * @param allow allow
     * @param id    id
     */
    void deleteDept(String allow, Long id);
}

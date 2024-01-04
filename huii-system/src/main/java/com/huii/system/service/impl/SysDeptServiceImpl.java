package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysDept;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Tree;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.system.domain.SysRoleDept;
import com.huii.system.mapper.SysDeptMapper;
import com.huii.system.mapper.SysRoleDeptMapper;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptMapper sysDeptMapper;
    private final SysUserMapper sysUserMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;

    @Override
    public List<SysDept> selectDeptList(SysDept sysDept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<SysDept>()
                .eq(ObjectUtils.isNotEmpty(sysDept.getDeptId()), SysDept::getDeptId, sysDept.getDeptId())
                .eq(ObjectUtils.isNotEmpty(sysDept.getParentId()), SysDept::getParentId, sysDept.getParentId())
                .eq(ObjectUtils.isNotEmpty(sysDept.getDeptName()), SysDept::getDeptName, sysDept.getDeptName())
                .eq(ObjectUtils.isNotEmpty(sysDept.getDeptStatus()), SysDept::getDeptStatus, sysDept.getDeptStatus())
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getDeptSeq);
        return sysDeptMapper.selectList(wrapper);
    }

    @Override
    public SysDept selectDeptById(Long id) {
        return sysDeptMapper.selectById(id);
    }

    @Override
    public List<SysDept> buildTree(List<SysDept> dept) {
        return treeBuilder(0L, dept);
    }

    @Override
    public List<Tree> buildSelect(List<SysDept> dept, Boolean addHeadNode) {
        List<Tree> children = dept.stream().map(Tree::new).collect(Collectors.toList());
        if (addHeadNode) {
            Tree tree = new Tree(0L, "顶级部门", children);
            List<Tree> list = new ArrayList<>(1);
            list.add(tree);
            return list;
        }
        return children;
    }

    @Override
    public List<Long> selectDeptIdsByRoleId(Long roleId) {
        List<SysRoleDept> list = sysRoleDeptMapper.selectList(new LambdaQueryWrapper<SysRoleDept>()
                .eq(SysRoleDept::getRoleId, roleId));
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(SysRoleDept::getDeptId)
                .collect(Collectors.toList());
    }

    @Override
    public void checkInsert(SysDept sysDept) {
        if (sysDeptMapper.exists(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeptName, sysDept.getDeptName()))) {
            ResType resType = ResType.SYS_DEPT_NAME_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void insertDept(SysDept sysDept) {
        sysDeptMapper.insert(sysDept);
    }

    @Override
    public void checkUpdate(SysDept sysDept) {
        SysDept oldOne = sysDeptMapper.selectById(sysDept.getDeptId());
        if (sysDept.getDeptId().equals(sysDept.getParentId())) {
            ResType resType = ResType.SYS_DEPT_NOT_ALLOW_SET_PARENT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (!StringUtils.equals(sysDept.getDeptName(), oldOne.getDeptName())) {
            if (sysDeptMapper.exists(new LambdaQueryWrapper<SysDept>()
                    .eq(SysDept::getDeptName, sysDept.getDeptName()))) {
                ResType resType = ResType.SYS_DEPT_NAME_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
    }

    @Override
    public void updateDept(SysDept sysDept) {
        sysDeptMapper.updateById(sysDept);
    }

    @Override
    public void deleteDept(String allow, Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (SystemConstants.STATUS_1.equals(allow)) {
            List<SysDept> list = sysDeptMapper.selectList(null);
            selectChildrenIds(id, list, ids);
            boolean existsUser = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                    .in(SysUser::getDeptId, ids));
            if (existsUser) {
                ResType resType = ResType.SYS_DEPT_EXISTS_USER;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            sysDeptMapper.deleteBatchIds(ids);
        } else {
            boolean existsChildren = sysDeptMapper.exists(new LambdaQueryWrapper<SysDept>()
                    .eq(SysDept::getParentId, id));
            if (existsChildren) {
                ResType resType = ResType.SYS_DEPT_EXISTS_CHILDREN;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            boolean existsUser = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getDeptId, id));
            if (existsUser) {
                ResType resType = ResType.SYS_DEPT_EXISTS_USER;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            sysDeptMapper.deleteById(id);
        }
    }

    private List<SysDept> treeBuilder(Long pid, List<SysDept> list) {
        List<SysDept> children = new ArrayList<>();
        for (SysDept dept : list) {
            if (pid.equals(dept.getParentId())) {
                dept.setChildrenFlag(false);
                children.add(dept);
            }
        }
        for (SysDept t : children) {
            List<SysDept> tList = treeBuilder(t.getDeptId(), list);
            if (!tList.isEmpty()) {
                t.setChildrenFlag(true);
            }
            t.setChildren(tList);
        }
        return children;
    }

    private void selectChildrenIds(Long pid, List<SysDept> list, List<Long> ids) {
        for (SysDept ele : list) {
            if (pid.equals(ele.getParentId())) {
                ids.add(ele.getDeptId());
                selectChildrenIds(ele.getDeptId(), list, ids);
            }
        }
    }
}

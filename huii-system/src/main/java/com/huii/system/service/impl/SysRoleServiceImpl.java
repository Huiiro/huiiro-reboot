package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.DataScopeType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysRoleDept;
import com.huii.system.domain.SysRoleMenu;
import com.huii.system.domain.SysUserRole;
import com.huii.system.mapper.SysRoleDeptMapper;
import com.huii.system.mapper.SysRoleMapper;
import com.huii.system.mapper.SysRoleMenuMapper;
import com.huii.system.mapper.SysUserRoleMapper;
import com.huii.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Page selectRoleList(SysRole sysRole, PageParam pageParam) {
        IPage<SysRole> iPage = new PageParamUtils<SysRole>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysRole)));
    }

    @Override
    public List<Label> selectRolesAll() {
        List<SysRole> roles = sysRoleMapper.selectList(null);
        return roles.stream()
                .filter(f -> SystemConstants.STATUS_1.equals(f.getRoleStatus()))
                .map(m -> new Label(m.getRoleId(), m.getRoleName())).toList();
    }

    @Override
    public List<Long> selectUserRoleIds() {
        return null;
    }

    @Override
    public SysRole selectRoleById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysRole sysRole) {
        if (sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleName, sysRole.getRoleName()))) {
            throw new ServiceException("角色名称重复");
        }
        if (sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, sysRole.getRoleKey()))) {
            throw new ServiceException("角色编码重复");
        }
    }

    @Override
    public void insertRole(SysRole sysRole) {
        sysRole.setRoleScope(DataScopeType.SELF.getCode());
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public void checkUpdate(SysRole sysRole) {
        SysRole oldOne = sysRoleMapper.selectById(sysRole.getRoleId());
        if (!StringUtils.equals(sysRole.getRoleName(), oldOne.getRoleName())) {
            if (sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleName, sysRole.getRoleName()))) {
                throw new ServiceException("角色名称重复");
            }
        }
        if (!StringUtils.equals(sysRole.getRoleKey(), oldOne.getRoleKey())) {
            if (sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleKey, sysRole.getRoleKey()))) {
                throw new ServiceException("角色编码重复");
            }
        }
    }

    @Override
    public void updateRole(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
    }

    @Override
    public void updateRoleStatus(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
    }

    @Override
    public void updateRoleAuths(SysRole sysRole) {
        List<Long> menuIds = sysRole.getMenuIdList();
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, sysRole.getRoleId()));

        List<SysRoleMenu> roleMenus = new ArrayList<>();
        for (Long id : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(id);
            sysRoleMenu.setRoleId(sysRole.getRoleId());
            roleMenus.add(sysRoleMenu);
        }
        sysRoleMenuMapper.insertBatch(roleMenus);
    }

    @Override
    public void updateRoleDataScope(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
        sysRoleDeptMapper.delete(new LambdaQueryWrapper<SysRoleDept>()
                .eq(SysRoleDept::getRoleId, sysRole.getRoleId()));
        List<Long> ids = sysRole.getDeptIdList();
        if (ids.isEmpty()) {
            return;
        }
        if (!DataScopeType.CUSTOM.getCode().equals(sysRole.getRoleScope())) {
            return;
        }
        List<SysRoleDept> list = new ArrayList<>();
        for (Long id : ids) {
            SysRoleDept roleDept = new SysRoleDept();
            roleDept.setRoleId(sysRole.getRoleId());
            roleDept.setDeptId(id);
            list.add(roleDept);
        }
        sysRoleDeptMapper.insertBatch(list);
    }

    @Override
    public void deleteRoles(Long[] ids) {
        for (Long id : ids) {
            boolean existUserInRole = sysUserRoleMapper.exists(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getRoleId, id));
            if (existUserInRole) {
                SysRole sysRole = selectRoleById(id);
                throw new ServiceException(sysRole.getRoleName() + "角色下存在用户，不允许删除");
            }
        }

        List<Long> roles = Arrays.asList(ids);
        sysRoleMapper.deleteBatchIds(roles);
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, roles));
        sysRoleDeptMapper.delete(new LambdaQueryWrapper<SysRoleDept>()
                .in(SysRoleDept::getRoleId, roles));
    }

    private LambdaQueryWrapper<SysRole> wrapperBuilder(SysRole role) {
        Map<String, Object> params = role.getParams();
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
                .like(StringUtils.isNotBlank(role.getRoleKey()), SysRole::getRoleKey, role.getRoleKey())
                .eq(ObjectUtils.isNotEmpty(role.getRoleScope()), SysRole::getRoleScope, role.getRoleScope())
                .eq(ObjectUtils.isNotEmpty(role.getRoleStatus()), SysRole::getRoleStatus, role.getRoleStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysRole::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysRole::getRoleSeq)
                .orderByAsc(SysRole::getCreateTime);
        return wrapper;
    }
}

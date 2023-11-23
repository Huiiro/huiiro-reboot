package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.model.Route;
import com.huii.common.core.model.Tree;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.mapper.SysMenuMapper;
import com.huii.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> selectMenuList(SysMenu sysMenu, Long userId) {
        List<SysMenu> list;
        if (SecurityUtils.isAdmin(userId)) {
            list = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .like(StringUtils.isNotBlank(sysMenu.getMenuName()), SysMenu::getMenuName, sysMenu.getMenuName())
                    .eq(StringUtils.isNotBlank(sysMenu.getMenuVisible()), SysMenu::getMenuVisible, sysMenu.getMenuVisible())
                    .eq(StringUtils.isNotBlank(sysMenu.getMenuStatus()), SysMenu::getMenuStatus, sysMenu.getMenuStatus())
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getMenuSeq));
        } else {
            sysMenu.setMenuStatus(SystemConstants.STATUS_1);
            list = sysMenuMapper.selectListMenusByUserId(sysMenu, userId);
        }
        return list;
    }

    @Override
    public List<Route> buildRoutes(List<SysMenu> list) {
        return routeBuilder(0L, list);
    }

    @Override
    public List<SysMenu> buildTree(List<SysMenu> menus) {
        return treeBuilder(0L, menus);
    }

    @Override
    public List<Tree> buildSelect(List<SysMenu> menus) {
        List<Tree> children = menus.stream().map(Tree::new).collect(Collectors.toList());
        Tree tree = new Tree(0L, "顶级菜单",children);
        List<Tree> list = new ArrayList<>(1);
        list.add(tree);
        return list;
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return sysMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public SysMenu selectMenuById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysMenu sysMenu) {
        if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuName, sysMenu.getMenuName()))) {
            throw new ServiceException("菜单名称字段重复");
        }
        if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuAuth, sysMenu.getMenuAuth()))) {
            throw new ServiceException("菜单权限字段重复");
        }
        if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuPath, sysMenu.getMenuPath()))) {
            throw new ServiceException("菜单路径字段重复");
        }
    }

    @Override
    public void insertMenu(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    @Override
    public void checkUpdate(SysMenu sysMenu) {
        SysMenu oldOne = sysMenuMapper.selectById(sysMenu.getMenuId());
        if (sysMenu.getMenuId().equals(sysMenu.getParentId())) {
            throw new ServiceException("不允许的操作,无法将自己作为上级菜单");
        }
        if (!StringUtils.equals(sysMenu.getMenuName(), oldOne.getMenuName())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuName, sysMenu.getMenuName()))) {
                throw new ServiceException("菜单名称字段重复");
            }
        }
        if (!StringUtils.equals(sysMenu.getMenuAuth(), oldOne.getMenuAuth())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuAuth, sysMenu.getMenuAuth()))) {
                throw new ServiceException("菜单权限字段重复");
            }
        }
        if (!StringUtils.equals(sysMenu.getMenuPath(), oldOne.getMenuPath())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuPath, sysMenu.getMenuPath()))) {
                throw new ServiceException("菜单路径字段重复");
            }
        }
    }

    @Override
    public void updateMenu(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public void deleteMenu(String allow, Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (SystemConstants.STATUS_1.equals(allow)) {
            List<SysMenu> menuList = sysMenuMapper.selectList(null);
            selectChildrenIds(id, menuList, ids);
            if (sysMenuMapper.checkRoleHasAnyMenu(ids)) {
                throw new ServiceException("存在角色已分配该菜单");
            }
            sysMenuMapper.deleteBatchIds(ids);
        } else {
            boolean existsChildren = sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getParentId, id));
            if (existsChildren) {
                throw new ServiceException("该菜单下存在子菜单");
            }
            if (sysMenuMapper.checkRoleHasAnyMenu(ids)) {
                throw new ServiceException("存在角色已分配该菜单");
            }
            sysMenuMapper.deleteById(id);
        }
    }

    private List<Route> routeBuilder(Long pid, List<SysMenu> list) {
        List<Route> children = new ArrayList<>();
        for (SysMenu menu : list) {
            if (pid.equals(menu.getParentId())) {
                Route route = Route.buildRoute(menu);
                route.setChildrenFlag(false);
                children.add(route);
            }
        }
        for (Route t : children) {
            List<Route> tList = routeBuilder(t.getId(), list);
            if (!tList.isEmpty()) {
                t.setChildrenFlag(true);
            }
            t.setChildren(tList);
        }
        return children;
    }

    private List<SysMenu> treeBuilder(Long pid, List<SysMenu> list) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu menu : list) {
            if (pid.equals(menu.getParentId())) {
                menu.setChildrenFlag(false);
                children.add(menu);
            }
        }
        for (SysMenu t : children) {
            List<SysMenu> tList = treeBuilder(t.getMenuId(), list);
            if (!tList.isEmpty()) {
                t.setChildrenFlag(true);
            }
            t.setChildren(tList);
        }
        return children;
    }

    private void selectChildrenIds(Long pid, List<SysMenu> list, List<Long> ids) {
        for (SysMenu ele : list) {
            if (pid.equals(ele.getParentId())) {
                ids.add(ele.getMenuId());
                selectChildrenIds(ele.getMenuId(), list, ids);
            }
        }
    }
}

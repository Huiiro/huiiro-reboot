package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.MenuConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.model.Route;
import com.huii.common.core.model.Tree;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.mapper.SysMenuMapper;
import com.huii.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<Tree> buildSelect(List<SysMenu> menus, Boolean addHeadNode) {
        List<Tree> children = menus.stream().map(Tree::new).collect(Collectors.toList());
        if (addHeadNode) {
            Tree tree = new Tree(0L, "顶级菜单", children);
            List<Tree> list = new ArrayList<>(1);
            list.add(tree);
            return list;
        }
        return children;
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
            ResType resType = ResType.SYS_MENU_NAME_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuAuth, sysMenu.getMenuAuth()))) {
            ResType resType = ResType.SYS_MENU_AUTH_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuPath, sysMenu.getMenuPath()))) {
            ResType resType = ResType.SYS_MENU_PATH_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
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
            ResType resType = ResType.SYS_MENU_NOT_ALLOW_SET_PARENT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (!StringUtils.equals(sysMenu.getMenuName(), oldOne.getMenuName())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuName, sysMenu.getMenuName()))) {
                ResType resType = ResType.SYS_MENU_NAME_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysMenu.getMenuAuth(), oldOne.getMenuAuth())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuAuth, sysMenu.getMenuAuth()))) {
                ResType resType = ResType.SYS_MENU_AUTH_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysMenu.getMenuPath(), oldOne.getMenuPath())) {
            if (sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getMenuPath, sysMenu.getMenuPath()))) {
                ResType resType = ResType.SYS_MENU_PATH_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
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
                ResType resType = ResType.SYS_MENU_EXISTS_ROLE;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            sysMenuMapper.deleteBatchIds(ids);
        } else {
            boolean existsChildren = sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getParentId, id));
            if (existsChildren) {
                ResType resType = ResType.SYS_MENU_EXISTS_CHILDREN;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            if (sysMenuMapper.checkRoleHasAnyMenu(ids)) {
                ResType resType = ResType.SYS_MENU_EXISTS_ROLE;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
            sysMenuMapper.deleteById(id);
        }
    }

    private List<Route> routeBuilder(Long pid, List<SysMenu> list) {
        List<Route> children = new ArrayList<>();
        for (SysMenu menu : list) {
            if (pid.equals(menu.getParentId()) &&
                    (MenuConstants.MENU.equals(menu.getMenuType()) ||
                            MenuConstants.DIR.equals(menu.getMenuType()))) {
                Route route = buildRoute(menu);
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

    private static Route buildRoute(SysMenu sysMenu) {
        Route route = new Route();
        route.setId(sysMenu.getMenuId());
        route.setParentId(sysMenu.getParentId());
        route.setName(sysMenu.getMenuName());
        route.setPath(sysMenu.getMenuPath());
        route.setComponent(sysMenu.getMenuComponent());
        route.setQueryParam(sysMenu.getQueryParam());
        route.setIcon(sysMenu.getMenuIcon());
        route.setVisible(sysMenu.getMenuVisible().equals(SystemConstants.STATUS_1));
        route.setChildrenFlag(false);
        HashMap<String, Object> map = new HashMap<>();
        map.put("invisible", sysMenu.getMenuVisible());
        map.put("breadcrumb", true);
        map.put("keepAlive", false);
        map.put("title", sysMenu.getMenuName());
        map.put("icon", sysMenu.getMenuIcon());
        route.setMeta(map);
        return route;
    }
}

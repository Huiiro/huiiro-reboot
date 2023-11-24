package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.model.R;
import com.huii.common.core.model.Route;
import com.huii.common.core.model.Tree;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final SysMenuService sysMenuService;

    @GetMapping("/list")
    public R<List<SysMenu>> getList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuService.selectMenuList(sysMenu, getUserId());
        return R.ok(list);
    }


    @GetMapping("/route")
    public R<List<Route>> getRouteList(SysMenu sysMenu) {
        List<SysMenu> menus = sysMenuService.selectMenuList(sysMenu, getUserId());
        List<Route> routes = sysMenuService.buildRoutes(menus);
        return R.ok(routes);
    }

    @GetMapping("/tree")
    public R<List<SysMenu>> getTreeList(SysMenu sysMenu) {
        List<SysMenu> menus = sysMenuService.selectMenuList(sysMenu, getUserId());
        List<SysMenu> list = sysMenuService.buildTree(menus);
        return R.ok(list);
    }

    @GetMapping("/select")
    public R<List<Tree>> getTreeSelect(SysMenu sysMenu) {
        List<SysMenu> menus = sysMenuService.selectMenuList(sysMenu, getUserId());
        List<SysMenu> tree = sysMenuService.buildTree(menus);
        List<Tree> list = sysMenuService.buildSelect(tree, true);
        return R.ok(list);
    }

    /**
     * 构建分配角色权限时前端所需的下拉框
     */
    @GetMapping("/select/role/{roleId}")
    public R<Map<String, Object>> getTreeSelectByRole(@PathVariable Long roleId) {
        List<SysMenu> menus = sysMenuService.selectMenuList(new SysMenu(), getUserId());
        List<SysMenu> tree = sysMenuService.buildTree(menus);
        Map<String, Object> map = new HashMap<>();
        map.put("tree", sysMenuService.buildSelect(tree, false));
        map.put("keys", sysMenuService.selectMenuIdsByRoleId(roleId));
        return R.ok(map);
    }

    @GetMapping(value = "/{id}")
    public R<SysMenu> getInfo(@PathVariable Long id) {
        return R.ok(sysMenuService.selectMenuById(id));
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加菜单", opType = OpType.INSERT)
    public R<Void> insertMenu(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.checkInsert(sysMenu);
        sysMenuService.insertMenu(sysMenu);
        return saveSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update")
    @Log(value = "更新菜单", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateMenu(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.checkUpdate(sysMenu);
        sysMenuService.updateMenu(sysMenu);
        return updateSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete/{allow}/{id}")
    @Log(value = "删除菜单", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteMenu(@PathVariable String allow, @PathVariable Long id) {
        sysMenuService.deleteMenu(allow, id);
        return deleteSuccess();
    }
}

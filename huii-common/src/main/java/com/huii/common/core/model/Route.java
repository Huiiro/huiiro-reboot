package com.huii.common.core.model;

import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    private Long id;

    private Long parentId;

    private String name;

    private String path;

    private String component;

    private String queryParam;

    private Map<String, Object> meta;

    private String icon;

    private boolean visible;

    private boolean childrenFlag;

    private List<Route> children = new ArrayList<>();

    public static Route buildRoute(SysMenu sysMenu) {
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
        map.put("breadcrumb", true);
        map.put("title", sysMenu.getMenuName());
        map.put("icon",sysMenu.getMenuIcon());
        route.setMeta(map);
        return route;
    }
}

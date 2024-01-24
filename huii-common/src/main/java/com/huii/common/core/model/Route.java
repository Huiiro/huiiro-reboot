package com.huii.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 路由实体类
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Serializable {

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

}

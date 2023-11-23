package com.huii.common.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huii.common.core.domain.SysDept;
import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.domain.SysRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huii
 */
@Data
@AllArgsConstructor
public class Tree implements Serializable {

    private Long id;

    private String label;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tree> children;

    public Tree(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(Tree::new).collect(Collectors.toList());
    }

    public Tree(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(Tree::new).collect(Collectors.toList());
    }

    public Tree(SysRole role) {
        this.id = role.getRoleId();
        this.label = role.getRoleName();
        this.children = null;
    }
}

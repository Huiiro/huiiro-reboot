package com.huii.common.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huii.common.core.domain.SysDept;
import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.domain.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树表实体类
 *
 * @author huii
 */
@Data
public class Tree implements Serializable {

    private Long id;

    private String label;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tree> children;

    public Tree(Long id, String label, List<Tree> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }

    /**
     * 提供函数式接口，适用于通用场景
     * <p>using example（示例）:</p>
     * <p>List<SysDept> deptList = new ArrayList<>();</p>
     * <p>List<Tree> treeList = Tree.mapToTree(deptList, SysDept::getDeptId, SysDept::getDeptName, SysDept::getChildren);</p>
     *
     * @param list             list
     * @param idFunction       idFunction
     * @param labelFunction    labelFunction
     * @param childrenFunction childrenFunction
     * @return treeList
     */
    public static <T> List<Tree> mapToTree(List<T> list, Function<T, Long> idFunction, Function<T, String> labelFunction, Function<T, List<T>> childrenFunction) {
        return list.stream()
                .map(item -> new Tree(idFunction.apply(item), labelFunction.apply(item), mapToTree(childrenFunction.apply(item), idFunction, labelFunction, childrenFunction)))
                .collect(Collectors.toList());
    }

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

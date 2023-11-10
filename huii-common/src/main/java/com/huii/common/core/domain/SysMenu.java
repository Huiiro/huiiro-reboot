package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.TreeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单 实体类
 * sys_menu
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends TreeEntity<SysMenu> {

    @TableId(value = "menu_id", type = IdType.AUTO)
    @ExcelColumn(value = "菜单ID")
    private Long menuId;

    /**
     * 菜单类型
     * 1：菜单
     * 2：目录
     * 3：权限
     * 4：按钮
     */
    @NotNull(message = "菜单类型不为空")
    @ExcelColumn(value = "菜单类型", convert = "1=菜单,2=目录,3=权限,4=按钮")
    private Integer menuType;

    @NotBlank(message = "菜单名称不为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过{max}个字符")
    @ExcelColumn("菜单名称")
    private String menuName;

    @NotBlank(message = "菜单权限不为空")
    @Size(min = 0, max = 255, message = "菜单权限长度不能超过{max}个字符")
    @ExcelColumn("菜单权限")
    private String menuAuth;

    @ExcelColumn("菜单图标")
    private String menuIcon;

    @NotBlank(message = "菜单路由地址不为空")
    @Size(min = 0, max = 255, message = "菜单路由地址不能超过{max}个字符")
    @ExcelColumn("菜单路由地址")
    private String menuPath;

    @ExcelColumn("菜单组件")
    @Size(min = 0, max = 200, message = "菜单组件路径不能超过{max}个字符")
    private String menuComponent;

    @NotNull(message = "菜单顺序不为空")
    @Size(min = 0, max = 999, message = "菜单顺序应在{min}-{max}之间")
    @ExcelColumn("菜单顺序")
    private Integer menuSeq;

    @NotNull(message = "请选择是否展示在菜单中")
    @ExcelColumn(value = "是否展示在菜单中", convert = "0=隐藏,1=显示")
    private Integer menuVisible;

    @ExcelColumn(value = "菜单状态", convert = "0=禁用,1=正常")
    private Integer menuStatus;

    @ExcelColumn("路由参数")
    private String queryParam;

    @ExcelColumn("菜单备注")
    private String remark;
}
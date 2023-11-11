package com.huii.common.core.model.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huii.common.annotation.ExcelColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<T> extends BaseEntity {

    @Serial
    @ExcelColumn(export = false)
    private static final long serialVersionUID = 1L;

    @ExcelColumn(value = "父ID")
    private Long parentId;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private String parentName;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Boolean childrenFlag;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
}
package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统配置 实体类
 * sys_config
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@KeySequence(value = "sys_config_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysConfig extends BaseEntity {

    @TableId(value = "config_id", type = IdType.INPUT)
    @ExcelColumn(value = "系统参数配置ID")
    private Long configId;

    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 50, message = "参数名称长度不能超过{max}个字符")
    @ExcelColumn(value = "系统参数名称")
    private String configName;

    @NotBlank(message = "参数键名不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过{max}个字符")
    @ExcelColumn(value = "系统参数键")
    private String configKey;

    @NotBlank(message = "参数键值不能为空")
    @ExcelColumn(value = "系统参数值")
    @Size(min = 0, max = 999, message = "参数键值长度不能超过{max}个字符")
    private String configValue;

    @ExcelColumn(value = "系统参数备注")
    private String configRemark;
}
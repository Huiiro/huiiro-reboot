package com.huii.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 文件 实体类
 * sys_file
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file")
@KeySequence(value = "sys_config_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysFile extends BaseEntity {

    @TableId(value = "file_id", type = IdType.INPUT)
    @ExcelProperty(value = "文件ID")
    private Long fileId;

    @ExcelProperty(value = "文件名称")
    private String fileName;

    @ExcelProperty(value = "文件原名")
    private String originName;

    @ExcelProperty(value = "文件大小")
    private String fileSize;

    @ExcelProperty(value = "文件后缀")
    private String fileSuffix;

    @ExcelProperty(value = "文件权限")
    private String fileAcl;

    @ExcelProperty(value = "文件md5")
    private String fileMd5;

    @ExcelProperty(value = "文件价格")
    private BigDecimal filePrice;

    @ExcelProperty(value = "文件直链")
    private String accessUrl;

    @ExcelProperty(value = "文件服务商")
    private String fileServer;

    @ExcelProperty(value = "文件状态")
    private String fileStatus;
}
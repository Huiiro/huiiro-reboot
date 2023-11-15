package com.huii.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件 实体类
 * sys_file
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_file")
public class SysFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ExcelProperty(value = "文件ID")
    private Long id;

    @ExcelProperty(value = "文件名称")
    private String fileName;

    @ExcelProperty(value = "文件类型")
    private String fileSuffix;

    @ExcelProperty(value = "文件大小")
    private String fileSize;

    @ExcelProperty(value = "文件直链")
    private String accessUrl;

    @ExcelProperty(value = "文件服务商")
    private String fileServer;

    @ExcelProperty(value = "文件状态")
    private String fileStatus;
}
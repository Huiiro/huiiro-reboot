package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
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
    @ExcelColumn(value = "文件ID")
    private Long id;

    @ExcelColumn(value = "文件名称")
    private String fileName;

    @ExcelColumn(value = "文件类型")
    private String fileSuffix;

    @ExcelColumn(value = "文件大小")
    private String fileSize;

    @ExcelColumn(value = "文件直链")
    private String accessUrl;

    @ExcelColumn(value = "文件服务商")
    private String fileServer;

    @ExcelColumn(value = "文件状态")
    private String fileStatus;
}
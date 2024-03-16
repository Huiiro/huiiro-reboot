package com.huii.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_oauth")
public class SysUserOauth implements Serializable {

    @TableId(type = IdType.INPUT)
    @ExcelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty(value = "第三方应用名称")
    private String oauthProvider;

    @ExcelProperty(value = "第三方应用身份凭证")
    private String oauthIdentify;

    @ExcelProperty(value = "第三方应用名称")
    private String oauthUserName;

    @ExcelProperty(value = "第三方应用头像")
    private String oauthUserAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

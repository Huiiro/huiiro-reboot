package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huii.common.annotation.ExcelColumn;
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
    @ExcelColumn(value = "用户ID")
    private Long userId;

    @ExcelColumn(value = "第三方应用名称")
    private String oauthProvider;

    @ExcelColumn(value = "第三方应用身份凭证")
    private Long oauthIdentify;

    @ExcelColumn(value = "第三方应用名称")
    private String oauthUserName;

    @ExcelColumn(value = "第三方应用头像")
    private String oauthUserAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ExcelColumn(export = false)
    private LocalDateTime createTime;
}

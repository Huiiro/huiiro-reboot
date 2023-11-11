package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * sys_user_post
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_post")
public class SysUserPost implements Serializable {

    @TableId(type = IdType.INPUT)
    @ExcelColumn(value = "用户ID")
    private Long userId;

    @ExcelColumn(value = "岗位ID")
    private Long postId;
}
package com.huii.message.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * msg_subscribe_user
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("msg_subscribe_user")
public class MsgSubscribeUser implements Serializable {

    @TableId(type = IdType.INPUT)
    @ExcelProperty(value = "订阅ID")
    private Long subId;

    @ExcelProperty(value = "用户ID")
    private Long userId;
}

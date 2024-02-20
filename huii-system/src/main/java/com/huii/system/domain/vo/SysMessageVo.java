package com.huii.system.domain.vo;

import lombok.Data;

@Data
public class SysMessageVo {

    private Long messageId;

    private Long sendId;

    private String sendUserName;

    private String sendUserAvatar;

    private Long receiveId;

    private String message;

    private String messageStatus;

    private String messageType;

    private String messageRead;

    private String createTime;

}

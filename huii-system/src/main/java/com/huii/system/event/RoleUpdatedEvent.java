package com.huii.system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 更新角色权限事件
 * @see com.huii.auth.listener.RoleUpdatedEventListener
 */
@Getter
public class RoleUpdatedEvent extends ApplicationEvent {

    private final Long roleId;

    private final List<String> auths;

    public RoleUpdatedEvent(Object source, Long roleId, List<String> auths) {
        super(source);
        this.roleId = roleId;
        this.auths = auths;
    }
}

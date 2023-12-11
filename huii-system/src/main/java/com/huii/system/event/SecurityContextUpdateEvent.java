package com.huii.system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 更新角色权限事件
 *
 * @author huii
 */
@Getter
public class SecurityContextUpdateEvent extends ApplicationEvent {

    private final Long roleId;

    private final Long menuId;

    private final List<String> auths;

    private final List<Long> userIds;


    public SecurityContextUpdateEvent(Object source, Long roleId, Long menuId, List<String> auths, List<Long> userIds) {
        super(source);
        this.roleId = roleId;
        this.menuId = menuId;
        this.auths = auths;
        this.userIds = userIds;
    }
}

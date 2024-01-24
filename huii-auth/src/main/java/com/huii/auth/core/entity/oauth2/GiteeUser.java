package com.huii.auth.core.entity.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * gitee用户类
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GiteeUser extends Oauth2User {

    private String avatar_url;
}

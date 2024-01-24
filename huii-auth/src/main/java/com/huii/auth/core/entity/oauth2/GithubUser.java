package com.huii.auth.core.entity.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * github用户类
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubUser extends Oauth2User {

    private String login;

    private String avatar_url;
}

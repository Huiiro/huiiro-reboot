package com.huii.auth.utils;

import com.huii.auth.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * jwt util
 *
 * @author huii
 * @version 2.3
 **/
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;
    private final long castTime = 60 * 60 * 1000;

    public Integer getSingle() {
        return jwtProperties.getSingle();
    }

    public Integer getAccess() {
        return jwtProperties.getAccess();
    }

    public Integer getRefresh() {
        return jwtProperties.getRefresh();
    }

    public Integer getDev() {
        return jwtProperties.getDev();
    }

    /**
     * UserID --> 生成Token
     *
     * @param claims Map<String, Object>
     * @param time   Long Token_time (ms)
     * @return token
     */
    public String createToken(Map<String, Object> claims, Long time) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getKey())
                .setIssuer(jwtProperties.getIssuer())
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setSubject(claims.get(jwtProperties.getKeyword()).toString())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .compact();
    }

    public String createToken(String key, Long time) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(jwtProperties.getKeyword(), key);
        return createToken(claims, time);
    }

    public String createSingleToken(Long id) {
        return createToken(id.toString(), ((long) jwtProperties.getSingle() * castTime));
    }

    public String createAccessToken(Long id) {
        return createToken(id.toString(), ((long) jwtProperties.getAccess() * castTime));
    }

    public String createRefreshToken(Long id) {
        return createToken(id.toString(), ((long) jwtProperties.getRefresh() * castTime));
    }

    public Map<String, Object> parseTokenToMap(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getKey()).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    public Object getClaimsItem(String token, String key) {
        Map<String, Object> map = parseTokenToMap(token);
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }

    public String getKey(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String str = (String) getClaimsItem(token, jwtProperties.getKeyword());
        return ObjectUtils.isEmpty(str) ? null : str;
    }

    public Long getId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String str = (String) getClaimsItem(token, jwtProperties.getKeyword());
        return ObjectUtils.isEmpty(str) ? null : Long.valueOf(str);
    }

    /**
     * 判断token是否可用
     *
     * @param token token
     * @return boolean available -> true
     */
    public Boolean isTokenAvailable(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtProperties.getKey()).parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 判断token是否将要过期
     *
     * @param token  token
     * @param minute minute
     * @return boolean willExpired -> true
     */
    public Boolean isTokenWillExpired(String token, Integer minute) {
        int min = ObjectUtils.isNotEmpty(minute) ? minute : jwtProperties.getAutoRefreshTime();
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getKey()).parseClaimsJws(token).getBody();
        Integer now = Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000));
        Integer exp = (Integer) claims.get("exp");
        int dif = exp - now;
        if (dif < 0) {
            return true;
        }
        return dif / 60 < min;
    }


    /**
     * 获取singleToken
     *
     * @param token  token
     * @param minute minute
     * @return newToken
     */
    public String refreshSingleToken(String token, Integer minute) {
        int min = ObjectUtils.isNotEmpty(minute) ? minute : jwtProperties.getAutoRefreshTime();
        Long id = Long.valueOf(getKey(token));
        if (this.isTokenWillExpired(token, min)) {
            return this.createSingleToken(id);
        }
        return null;
    }

    /**
     * 获取accessToken
     *
     * @param refresh refresh
     * @param access  access
     * @param minute  minute
     * @return newToken
     */
    public String refreshAccessToken(String refresh, String access, Integer minute) {
        int min = ObjectUtils.isNotEmpty(minute) ? minute : jwtProperties.getAutoRefreshTime();
        Long id = Long.valueOf(getKey(refresh));
        if (this.isTokenWillExpired(access, min)) {
            return this.createAccessToken(id);
        }
        return null;
    }
}

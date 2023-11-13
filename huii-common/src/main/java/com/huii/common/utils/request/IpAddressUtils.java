package com.huii.common.utils.request;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip地址查询工具
 *
 * @author huii
 */
public class IpAddressUtils {

    public static final String UNKNOWN = "unknown";

    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 将地址转换为inet类型
     */
    public static InetAddress getINetAddress(HttpServletRequest request) {
        try {
            return InetAddress.getByName(getIp(request));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询IP归属地
     *
     * @param ip ip
     * @return position
     */
    public static String getAddr(String ip) {
        String query = RegionUtils.query(ip);
        return ObjectUtils.isEmpty(query) ? "" : query;
    }
}
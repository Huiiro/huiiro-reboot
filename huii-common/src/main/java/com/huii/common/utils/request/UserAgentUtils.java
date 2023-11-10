package com.huii.common.utils.request;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 获取UserAgent信息
 *
 * @author huii
 */
@Component
public class UserAgentUtils {

    /**
     * 根据http获取userAgent信息
     */
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 根据request获取userAgent，然后解析出osVersion
     */
    public String getOsVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsVersion(userAgent);
    }

    /**
     * 根据userAgent解析出osVersion
     */
    public String getOsVersion(String userAgent) {
        String osVersion = "";
        if (StringUtils.isBlank(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf("(") + 1,
                userAgent.indexOf(")")).split(";");
        if (strArr.length == 0) {
            return osVersion;
        }

        osVersion = strArr[1];
        return osVersion;
    }

    /**
     * 获取操作系统对象
     */
    private OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        return agent.getOperatingSystem();
    }


    /**
     * 获取os：Windows/ios/Android
     */
    public String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOs(userAgent);
    }

    /**
     * 获取os：Windows/ios/Android
     */
    public String getOs(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getGroup().getName();
    }


    /**
     * 获取deviceType
     */
    public String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceType(userAgent);
    }

    /**
     * 获取deviceType
     */
    public String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getDeviceType().toString();
    }

    /**
     * 获取操作系统的名字
     */
    public String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字
     */
    public String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getName();
    }

    /**
     * 获取device的生产厂家
     */
    public String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     */
    public String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getManufacturer().toString();
    }

    /**
     * 获取浏览器对象
     */
    public Browser getBrowser(String agent) {
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        return userAgent.getBrowser();
    }

    /**
     * 获取browser name
     */
    public String getBorderName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderName(userAgent);
    }

    /**
     * 获取browser name
     */
    public String getBorderName(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getName();
    }

    /**
     * 获取浏览器的类型
     */
    public String getBorderType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderType(userAgent);
    }

    /**
     * 获取浏览器的类型
     */
    public String getBorderType(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getBrowserType().getName();
    }

    /**
     * 获取浏览器组： CHROME、IE
     */
    public String getBorderGroup(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderGroup(userAgent);
    }

    /**
     * 获取浏览器组： CHROME、IE
     */
    public String getBorderGroup(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getGroup().getName();
    }

    /**
     * 获取浏览器的生产厂商
     */
    public String getBrowserManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserManufacturer(userAgent);
    }

    /**
     * 获取浏览器的生产厂商
     */
    public String getBrowserManufacturer(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getManufacturer().getName();
    }

    /**
     * 获取浏览器使用的渲染引擎
     */
    public String getBorderRenderingEngine(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderRenderingEngine(userAgent);
    }

    /**
     * 获取浏览器使用的渲染引擎
     */
    public String getBorderRenderingEngine(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getRenderingEngine().name();
    }

    /**
     * 获取浏览器版本
     */
    public String getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserVersion(userAgent);
    }

    /**
     * 获取浏览器版本
     */
    public String getBrowserVersion(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getVersion(userAgent).toString();
    }
}


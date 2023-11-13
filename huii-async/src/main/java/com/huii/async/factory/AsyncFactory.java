package com.huii.async.factory;

import com.huii.common.enums.LoginType;
import com.huii.common.utils.SpringUtils;
import com.huii.common.utils.request.HttpRequestUtils;
import com.huii.common.utils.request.IpAddressUtils;
import com.huii.system.domain.SysLogLogin;
import com.huii.system.domain.SysLogOp;
import com.huii.system.mapper.SysLogLoginMapper;
import com.huii.system.mapper.SysLogOpMapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * 异步任务工厂
 *
 * @author huii
 */
@Slf4j
public class AsyncFactory {

    public static TimerTask loginLogger(final String username, final String principal, final Integer type,
                                        final String status, final String message, final Object... args) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpServletRequest request = HttpRequestUtils.getRequest();
                    UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                    String browser = ua.getBrowser().getName();
                    String system = ua.getOperatingSystem().getName();
                    String ip = IpAddressUtils.getIp(request);
                    String addr = IpAddressUtils.getAddr(ip);

                    SysLogLogin sysLogLogin = new SysLogLogin(null, username, ip, addr, LocalDateTime.now(),
                            browser, system, type, status, message, null);
                    SpringUtils.getBean(SysLogLoginMapper.class).insert(sysLogLogin);
                    log.info("登录用户：{},登陆凭证：{},登录类型：{},登陆结果：{},登录信息：{}",
                            username, principal, LoginType.getLoginType(type), status, message);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        };
    }

    public static TimerTask apiLogger(final SysLogOp sysLogOp) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SpringUtils.getBean(SysLogOpMapper.class).insert(sysLogOp);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        };
    }
}

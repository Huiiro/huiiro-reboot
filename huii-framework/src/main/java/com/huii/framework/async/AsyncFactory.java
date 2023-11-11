package com.huii.framework.async;

import com.huii.common.enums.LoginType;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步任务工厂
 *
 * @author huii
 */
@Slf4j
public class AsyncFactory {

//    public static TimerTask loginLogger(final Long id, final String username, final String principal, final Integer type, final Integer status, final String message, final Object... args) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                try {
////                    HttpServletRequest request = RequestUtil.getRequest();
////                    UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
////                    String ip = IpAddressUtils.getIp(request);
////                    String position = IpAddressUtils.getAddr(ip);
////                    String system = ua.getOperatingSystem().getName();
////                    String browser = ua.getBrowser().getName();
////                    SysLogLogin sysLog = new SysLogLogin(null, id, username, LocalDateTime.now(), ip, position, system, browser,
////                            LoginType.getLoginResult(status), Objects.requireNonNull(LoginType.getLoginType(type)).getMethod(), message, null);
////                    SpringUtils.getBean(SysLogLoginService.class).asyncSaveLoginLog(sysLog);
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                } finally {
//                    log.info("登录用户：{},登陆凭证：{},登录类型：{},登陆结果：{},登录信息：{}", username, principal, LoginType.getLoginType(type), LoginType.getLoginResult(status), message);
//                }
//            }
//        };
//    }

    /**
     * 异步操作日志记录
     */
//    public static TimerTask apiLogger(final SysLogApi sysLogApi) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                try {
////                    SpringUtils.getBean(SysLogApiMapper.class).insert(sysLogApi);
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            }
//        };
//    }
}

package com.huii.controller.monitor;

import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.framework.core.domain.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务器监控控制层
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/system/server")
@RequiredArgsConstructor
public class SysServerController extends BaseController {

    @GetMapping("/info")
    public R<Server> getServerInfo() {
        Server server = new Server();
        server.getServerInfo();
        return R.ok(server);
    }
}

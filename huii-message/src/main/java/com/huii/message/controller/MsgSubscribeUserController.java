package com.huii.message.controller;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.message.domain.MsgSubscribe;
import com.huii.message.service.MsgSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户消息订阅控制层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Validated
@RestController
@RequestMapping("/msg/sub/user")
@RequiredArgsConstructor
public class MsgSubscribeUserController extends BaseController {

    private final MsgSubscribeService msgSubscribeService;

    /**
     * 查询订阅分页
     */
    @GetMapping("/list")
    public R<Page> getList(MsgSubscribe msgSubscribe, PageParam pageParam) {
        Page page = msgSubscribeService.selectMsgSubscribeUserList(getUserId(), msgSubscribe, pageParam);
        return R.ok(page);
    }

    /**
     * 订阅/取消订阅
     */
    @GetMapping("/{id}")
    public R<Boolean> subscribe(@PathVariable Long id) {
        Boolean b = msgSubscribeService.subscribe(getUserId(), id);
        return R.ok(b);
    }

    /**
     * 查询订阅状态
     */
    @GetMapping("/status/{id}")
    public R<Boolean> querySubscribeStatus(@PathVariable Long id) {
        Boolean b = msgSubscribeService.querySubscribeStatus(getUserId(), id);
        return R.ok(b);
    }
}

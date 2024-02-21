package com.huii.message.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.message.domain.MsgSubscribe;
import com.huii.message.service.MsgSubscribeService;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息订阅控制层实现
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Validated
@RestController
@RequestMapping("/msg/sub")
@RequiredArgsConstructor
public class MsgSubscribeController extends BaseController {

    private final MsgSubscribeService msgSubscribeService;
    private final SysUserService sysUserService;

    /**
     * 查询订阅分页
     */
    @GetMapping("/list")
    public R<Page> getList(MsgSubscribe msgSubscribe, PageParam pageParam) {
        Page page = msgSubscribeService.selectMsgSubscribeList(msgSubscribe, pageParam);
        return R.ok(page);
    }

    /**
     * 查询订阅名称
     */
    @GetMapping("/label")
    public R<List<Label>> getLabel() {
        List<Label> list = msgSubscribeService.getLabelList();
        return R.ok(list);
    }

    /**
     * 查询订阅
     */
    @GetMapping(value = "/{id}")
    public R<MsgSubscribe> getMsgSubscribe(@PathVariable Long id) {
        return R.ok(msgSubscribeService.selectMsgSubscribeById(id));
    }

    /**
     * 添加订阅
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:add')")
    @PostMapping("/insert")
    @Log(value = "添加订阅", opType = OpType.INSERT)
    public R<Void> insertMsgSubscribe(@Validated @RequestBody MsgSubscribe msgSubscribe) {
        msgSubscribeService.insertMsgSubscribe(msgSubscribe);
        return saveSuccess();
    }

    /**
     * 更新订阅
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:edit')")
    @PostMapping("/update")
    @Log(value = "更新订阅", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateMsgSubscribe(@Validated @RequestBody MsgSubscribe msgSubscribe) {
        msgSubscribeService.updateMsgSubscribe(msgSubscribe);
        return updateSuccess();
    }

    /**
     * 删除订阅
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:delete')")
    @PostMapping("/delete")
    @Log(value = "删除订阅", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteMsgSubscribe(@RequestBody Long[] ids) {
        msgSubscribeService.deleteMsgSubscribe(ids);
        return deleteSuccess();
    }

    /**
     * 查询未分配用户
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:query')")
    @GetMapping("/query/non/{sid}")
    public R<Page> queryNonSubdcribeUser(SysUser sysUser, PageParam pageParam, @PathVariable Long sid) {
        List<Long> userIds = msgSubscribeService.queryNonSubscribeUserIds(sid);
        Page page = sysUserService.queryUserSubscribe(sysUser, pageParam, userIds);
        return R.ok(page);
    }

    /**
     * 查询已分配用户
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:query')")
    @GetMapping("/query/sub/{sid}")
    public R<Page> querySubdcribeUser(SysUser sysUser, PageParam pageParam, @PathVariable Long sid) {
        List<Long> userIds = msgSubscribeService.querySubscribeUserIds(sid);
        Page page = sysUserService.queryUserSubscribe(sysUser, pageParam, userIds);
        return R.ok(page);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:edit')")
    @PostMapping("/user/unsub/{sid}")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> subdcribeUser(@PathVariable Long sid, @RequestBody Long[] userIds) {
        msgSubscribeService.subscribeUser(sid, userIds);
        return R.ok();
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ap.hasAuth('tool:msg:sub:edit')")
    @PostMapping("/user/sub/{sid}")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> unsubdcribeUser(@PathVariable Long sid, @RequestBody Long[] userIds) {
        msgSubscribeService.unsubscribeUser(sid, userIds);
        return R.ok();
    }
}

package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.SysPost;
import com.huii.common.core.domain.vo.SysPostExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.system.service.SysPostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
public class SysPostController extends BaseController {

    private final SysPostService sysPostService;

    /**
     * 导出岗位
     */
    @PreAuthorize("@ap.hasAuth('system:post:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出岗位", opType = OpType.EXPORT)
    public void exportPost(SysPost sysPost, HttpServletResponse response) {
        List<SysPost> list = sysPostService.selectPostList(sysPost);
        List<SysPostExportVo> vos = BeanCopyUtils.copyList(list, SysPostExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysPostExportVo.class, response);
    }
    /**
     * 获取岗位列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysPost sysPost, PageParam pageParam) {
        Page page = sysPostService.selectPostList(sysPost, pageParam);
        return R.ok(page);
    }

    /**
     * 获取岗位
     */
    @GetMapping(value = "/{id}")
    public R<SysPost> getPost(@PathVariable Long id) {
        return R.ok(sysPostService.selectPostById(id));
    }

    /**
     * 添加岗位
     */
    @PreAuthorize("@ap.hasAuth('system:post:add')")
    @PostMapping("/insert")
    @Log(value = "添加岗位", opType = OpType.INSERT)
    public R<SysPost> insertPost(@Validated @RequestBody SysPost sysPost) {
        sysPostService.checkInsert(sysPost);
        sysPostService.insertPost(sysPost);
        return saveSuccess();
    }

    /**
     * 更新岗位
     */
    @PreAuthorize("@ap.hasAuth('system:post:edit')")
    @PostMapping("/update")
    @Log(value = "更新岗位", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysPost> updatePost(@Validated @RequestBody SysPost sysPost) {
        sysPostService.checkUpdate(sysPost);
        sysPostService.updatePost(sysPost);
        return updateSuccess();
    }

    /**
     * 删除岗位
     */
    @PreAuthorize("@ap.hasAuth('system:post:delete')")
    @PostMapping("/delete")
    @Log(value = "删除岗位", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysPost> deletePost(@RequestBody Long[] ids) {
        sysPostService.deletePosts(ids);
        return deleteSuccess();
    }
}

package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.core.service.OssService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.enums.UploadType;
import com.huii.oss.utils.ImageWatermarkUtils;
import com.huii.system.domain.SysFile;
import com.huii.system.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 系统文件控制层
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/system/file")
@RequiredArgsConstructor
public class SysFileController extends BaseController {

    private final SysFileService sysFileService;
    private final LocalService localService;
    private final OssService ossService;

    /**
     * 上传文件
     */
    @PreAuthorize("@ap.hasAuth('system:file:import')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/upload/{type}")
    @Log(value = "上传文件", opType = OpType.IMPORT)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<UploadResult> uploadSysFile(@RequestPart("file") MultipartFile file, @PathVariable String type,
                                         @RequestParam(required = false) String watermark) {
        MultipartFile multipartFile = file;
        if (null != watermark) {
            Long userId = getUserId();
            String wmText = "Huii-Reboot3 UID：" + userId;
            multipartFile = ImageWatermarkUtils.markWithContent(file, wmText);
        }
        UploadResult uploadResult;
        type = type.trim();
        if (UploadType.OSS.getName().equals(type)) {
            uploadResult = ossService.uploadFile(multipartFile);
        } else {
            uploadResult = localService.uploadFile(multipartFile);
        }
        sysFileService.uploadSysFile(uploadResult.getFilename(), uploadResult.getFileOriginName(),
                uploadResult.getUrl(), uploadResult.getFileSize(), uploadResult.getMd5(), type);
        return R.ok(uploadResult);
    }

    /**
     * 查询文件分页
     */
    @GetMapping("/list")
    public R<Page> getList(SysFile sysFile, PageParam pageParam) {
        Page page = sysFileService.selectSysFileList(sysFile, pageParam);
        return R.ok(page);
    }

    /**
     * 更新文件权限
     */
    @PreAuthorize("@ap.hasAuth('system:file:edit')")
    @PostMapping("/update/acl")
    @Log(value = "更新文件权限", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateSysFileAcl(@Validated @RequestBody SysFile sysFile) {
        sysFileService.updateSysFileAcl(sysFile);
        return updateSuccess();
    }

    /**
     * 更新文件状态
     */
    @PreAuthorize("@ap.hasAuth('system:file:edit')")
    @PostMapping("/update/status")
    @Log(value = "更新文件状态", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateSysFileStatus(@Validated @RequestBody SysFile sysFile) {
        sysFileService.updateSysFileStatus(sysFile);
        return updateSuccess();
    }

    /**
     * 删除文件
     */
    @PreAuthorize("@ap.hasAuth('system:file:delete')")
    @PostMapping("/delete")
    @Log(value = "删除文件", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteSysFile(@RequestBody Long[] ids) {
        Map<String, List<String>> map = sysFileService.sortFileIdsByServer(ids);
        if (map.containsKey(UploadType.LOCAL.getName())) {
            localService.deleteBatch(map.get(UploadType.LOCAL.getName()));
        }
        if (map.containsKey(UploadType.OSS.getName())) {
            ossService.deleteBatch(map.get(UploadType.OSS.getName()));
        }
        sysFileService.deleteSysFile(ids);
        return deleteSuccess();
    }
}
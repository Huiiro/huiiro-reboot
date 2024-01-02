package com.huii;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.oss.core.service.OssService;
import com.huii.oss.entity.UploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Anonymous
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class OssTestController extends BaseController {

    private final OssService ossService;
    
    @RequestMapping("/upload")
    public R<Object> upload(@RequestParam("file") MultipartFile file) {
        UploadResult uploadResult = ossService.uploadFile(file);
        return R.ok(uploadResult);
    }

    @RequestMapping("/get/{file}")
    public R<Object> getFile(@PathVariable String file) {
        return R.ok(ossService.getPreSignedUrl(file));
    }
}

package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.FileAcl;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysFile;
import com.huii.system.mapper.SysFileMapper;
import com.huii.system.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件服务层实现
 *
 * @author huii
 * @date 2024-01-03T16:32:39
 */
@Service
@RequiredArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    private final SysFileMapper sysFileMapper;

    @Override
    public void uploadSysFile(String filename, String fileOriginName, String url, String fileSize, String md5, String type) {
        SysFile file = new SysFile();
        file.setFileName(filename);
        file.setOriginName(fileOriginName);
        file.setFileSize(fileSize);
        file.setFileServer(type);
        file.setFileMd5(md5);
        file.setAccessUrl(url);
        file.setFileAcl(FileAcl.PUBLIC.getAcl());
        file.setFileStatus(SystemConstants.STATUS_1);
        sysFileMapper.insert(file);
    }

    @Override
    public Page selectSysFileList(SysFile sysFile, PageParam pageParam) {
        IPage<SysFile> iPage = new PageParamUtils<SysFile>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysFile)));
    }

    @Override
    public List<SysFile> selectSysFileList(SysFile sysFile) {
        return sysFileMapper.selectList(wrapperBuilder(sysFile));
    }

    @Override
    public SysFile selectSysFileById(Long id) {
        return sysFileMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysFile sysFile) {
    }

    @Override
    public void insertSysFile(SysFile sysFile) {
        sysFileMapper.insert(sysFile);
    }

    @Override
    public void checkUpdate(SysFile sysFile) {
    }

    @Override
    public void updateSysFile(SysFile sysFile) {
        sysFileMapper.updateById(sysFile);
    }

    @Override
    public void updateSysFileAcl(SysFile sysFile) {
        sysFileMapper.updateById(sysFile);
    }

    @Override
    public void updateSysFileStatus(SysFile sysFile) {
        sysFileMapper.updateById(sysFile);
    }

    @Override
    public Map<String, List<String>> sortFileIdsByServer(Long[] ids) {
        List<SysFile> files = sysFileMapper.selectBatchIds(Arrays.asList(ids));
        return files.stream()
                .collect(Collectors.groupingBy(SysFile::getFileServer,
                        Collectors.mapping(SysFile::getAccessUrl, Collectors.toList())));
    }

    @Override
    public void deleteSysFile(Long[] ids) {
        sysFileMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<SysFile> wrapperBuilder(SysFile sysFile) {
        Map<String, Object> params = sysFile.getParams();
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(sysFile.getFileId()), SysFile::getFileId, sysFile.getFileId())
                .like(ObjectUtils.isNotEmpty(sysFile.getFileName()), SysFile::getFileName, sysFile.getFileName())
                .like(ObjectUtils.isNotEmpty(sysFile.getOriginName()), SysFile::getOriginName, sysFile.getOriginName())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFileSize()), SysFile::getFileSize, sysFile.getFileSize())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFileSuffix()), SysFile::getFileSuffix, sysFile.getFileSuffix())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFileAcl()), SysFile::getFileAcl, sysFile.getFileAcl())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFilePrice()), SysFile::getFilePrice, sysFile.getFilePrice())
                .eq(ObjectUtils.isNotEmpty(sysFile.getAccessUrl()), SysFile::getAccessUrl, sysFile.getAccessUrl())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFileServer()), SysFile::getFileServer, sysFile.getFileServer())
                .eq(ObjectUtils.isNotEmpty(sysFile.getFileStatus()), SysFile::getFileStatus, sysFile.getFileStatus())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        SysFile::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(SysFile::getCreateTime);
        return wrapper;
    }
}

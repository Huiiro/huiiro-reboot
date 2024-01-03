package com.huii.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务层接口
 *
 * @author huii
 * @date 2024-01-03T16:32:39
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param filename 文件名称
     * @param url      文件链接
     */
    void uploadSysFile(String filename, String fileOriginName, String url, String fileSize, String md5, String type);

    /**
     * 查询file分页
     *
     * @param sysFile   sysFile
     * @param pageParam pageParam
     * @return page
     */
    Page selectSysFileList(SysFile sysFile, PageParam pageParam);

    /**
     * 查询file列表
     *
     * @param sysFile sysFile
     * @return list
     */
    List<SysFile> selectSysFileList(SysFile sysFile);

    /**
     * 查询file
     *
     * @param id id
     * @return sysFile
     */
    SysFile selectSysFileById(Long id);

    /**
     * 校验添加file数据
     *
     * @param sysFile sysFile
     */
    void checkInsert(SysFile sysFile);

    /**
     * 添加file
     *
     * @param sysFile sysFile
     */
    void insertSysFile(SysFile sysFile);

    /**
     * 校验修改file数据
     *
     * @param sysFile sysFile
     */
    void checkUpdate(SysFile sysFile);

    /**
     * 修改file
     *
     * @param sysFile sysFile
     */
    void updateSysFile(SysFile sysFile);

    /**
     * 更新文件权限
     *
     * @param sysFile sysFile
     */
    void updateSysFileAcl(SysFile sysFile);

    /**
     * 更新文件状态
     *
     * @param sysFile sysFile
     */
    void updateSysFileStatus(SysFile sysFile);

    /**
     * 根据服务提供商分类文件ID
     *
     * @param ids ids
     * @return id group
     */
    Map<String, List<String>> sortFileIdsByServer(Long[] ids);

    /**
     * 删除file
     *
     * @param ids ids
     */
    void deleteSysFile(Long[] ids);


}

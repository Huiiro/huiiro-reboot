package com.huii.system.listener;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.huii.common.constants.UserConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.domain.vo.SysUserExcelImportVo;
import com.huii.common.excel.ExcelListener;
import com.huii.common.excel.ExcelResult;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.SecurityUtils;
import com.huii.common.utils.SpringUtils;
import com.huii.common.utils.ValidatorUtils;
import com.huii.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * 自定义用户导入监听器
 *
 * @author huii
 */
@Slf4j
public class SysUserImportListener extends AnalysisEventListener<SysUserExcelImportVo> implements ExcelListener<SysUserExcelImportVo> {

    private final SysUserMapper sysUserMapper;
    private final Boolean isUpdateSupport;
    private final String password;
    private final String createBy;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public SysUserImportListener(Boolean isUpdateSupport) {
        this.isUpdateSupport = isUpdateSupport;
        this.password = SecurityUtils.encryptPassword(UserConstants.USER_DEFAULT_PASSWORD);
        this.createBy = SecurityUtils.getUsername();
        this.sysUserMapper = SpringUtils.getBean(SysUserMapper.class);
    }

    @Override
    public ExcelResult<SysUserExcelImportVo> getExcelResult() {
        return new ExcelResult<>() {
            @Override
            public List<SysUserExcelImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }

            @Override
            public String getAnalysis() {
                if (failureNum + successNum <= 0) {
                    return "暂无更多数据";
                }
                if (failureNum > 0) {
                    failureMsg.insert(0, "共 " + successNum + " 条数据读取成功，共 " + failureMsg + " 条数据读取失败，失败数据如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "共 " + successNum + " 条数据已全部导入成功，数据如下：");
                }
                return successMsg.toString();
            }
        };
    }

    @Override
    public void invoke(SysUserExcelImportVo vo, AnalysisContext analysisContext) {
        SysUser user = this.sysUserMapper.selectUserByUserName(vo.getUserName());
        try {
            if (ObjectUtils.isEmpty(user)) {
                successNum += insertUser(vo);
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
            } else if (isUpdateSupport) {
                successNum += updateUser(vo, user);
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
            } else {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getUserName()).append(" 已存在");
            }
        } catch (Exception e) {
            failureNum++;
            failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getUserName()).append(" 导入失败：").append(e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    private int insertUser(SysUserExcelImportVo vo) {
        SysUser user = BeanUtil.toBean(vo, SysUser.class);
        ValidatorUtils.validate(user);
        user.setPassword(password);
        user.setCreateBy(createBy);
        return sysUserMapper.insert(user);
    }

    private int updateUser(SysUserExcelImportVo vo, SysUser user) {
        SysUser userVo = BeanUtil.toBean(vo, SysUser.class);
        userVo.setUserId(user.getUserId());
        userVo.setUpdateBy(createBy);
        return sysUserMapper.updateById(userVo);
    }
}

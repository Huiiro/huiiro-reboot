package com.huii.auth.core.service.impl;

import com.huii.auth.core.entity.RegisterEntity;
import com.huii.auth.core.service.LoginSecurityService;
import com.huii.auth.core.service.RegisterService;
import com.huii.common.constants.SystemConstants;
import com.huii.common.constants.UserConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * 注册服务实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final SysUserMapper sysUserMapper;
    private final LoginSecurityService loginSecurityService;

    @Override
    public Boolean checkUsername(String username) {
        SysUser sysUser = sysUserMapper.selectUserByUserName(username);
        return ObjectUtils.isEmpty(sysUser);
    }

    @Override
    public void register(RegisterEntity entity) {
        if (!checkUsername(entity.getUsername())) {
            throw new ServiceException(MessageUtils.message(ResType.USER_REGISTER_NAME_REPEAT.getI18n()));
        }
        if (entity.getUsername().contains("@") || entity.getUsername().contains("$")) {
            throw new ServiceException(MessageUtils.message(ResType.USER_REGISTER_NAME_ILLEGAL.getI18n()));
        }
        String decryptPassword = loginSecurityService.decrypt(entity.getPassword());
        String encryptPassword = SecurityUtils.encryptPassword(decryptPassword);
        SysUser sysUser = new SysUser();
        sysUser.setUserName(entity.getUsername());
        sysUser.setNickName(entity.getUsername());
        sysUser.setPassword(encryptPassword);
        sysUser.setUserStatus(SystemConstants.STATUS_1);
        sysUser.setDeleteFlag(SystemConstants.STATUS_0);
        sysUser.setSexual(UserConstants.USER_DEFAULT_SEXUAL);
        sysUser.setAvatar(UserConstants.USER_DEFAULT_AVATAR);
        sysUser.setCreateBy(entity.getUsername());
        sysUser.setUpdateBy(entity.getUsername());

        sysUserMapper.insert(sysUser);
    }
}

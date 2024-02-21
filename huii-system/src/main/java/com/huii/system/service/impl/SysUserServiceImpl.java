package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.constants.UserConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.PageParam;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.system.domain.SysUserPost;
import com.huii.system.domain.SysUserRole;
import com.huii.system.mapper.SysDeptMapper;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserPostMapper;
import com.huii.system.mapper.SysUserRoleMapper;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserPostMapper sysUserPostMapper;
    private final SysDeptMapper sysDeptMapper;

    @Override
    public List<SysUser> selectUserList(SysUser sysUser) {
        return sysUserMapper.selectUserListWithDept(wrapperBuilder(sysUser));
    }

    @Override
    public com.huii.common.core.model.Page selectUserList(SysUser sysUser, PageParam pageParam) {
        Page<SysUser> page = new PageParamUtils<SysUser>().getPage(pageParam);
        Page<SysUser> userPage = sysUserMapper.selectUserPageWithDept(page, wrapperBuilder(sysUser));
        return new com.huii.common.core.model.Page(userPage);
    }

    @Override
    public SysUser selectUserById(Long id) {
        return sysUserMapper.selectUserVoById(id);
    }

    @Override
    public void checkInsert(SysUser sysUser) {
        if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, sysUser.getUserName()))) {
            ResType resType = ResType.SYS_USER_NAME_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, sysUser.getPhone()))) {
            ResType resType = ResType.SYS_USER_PHONE_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, sysUser.getEmail()))) {
            ResType resType = ResType.SYS_USER_EMAIL_REPEAT;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void insertUser(SysUser sysUser) {
        sysUser.setDeleteFlag(SystemConstants.STATUS_0);
        sysUser.setPassword(SecurityUtils.encryptPassword(UserConstants.USER_DEFAULT_PASSWORD));
        Long userId = sysUser.getUserId();
        insertUserRole(userId, sysUser.getRoleIds());
        insertUserPost(userId, sysUser.getPostIds());
        sysUserMapper.insert(sysUser);
    }

    @Override
    public void checkUpdate(SysUser sysUser) {
        SysUser oldOne = sysUserMapper.selectById(sysUser.getUserId());
        if (!StringUtils.equals(sysUser.getUserName(), oldOne.getUserName())) {
            if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserName, sysUser.getUserName()))) {
                ResType resType = ResType.SYS_USER_NAME_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysUser.getPhone(), oldOne.getPhone())) {
            if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, sysUser.getPhone()))) {
                ResType resType = ResType.SYS_USER_PHONE_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
        if (!StringUtils.equals(sysUser.getEmail(), oldOne.getEmail())) {
            if (sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getEmail, sysUser.getEmail()))) {
                ResType resType = ResType.SYS_USER_EMAIL_REPEAT;
                throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
            }
        }
    }

    @Override
    public void updateUser(SysUser sysUser) {
        Long userId = sysUser.getUserId();
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        insertUserRole(userId, sysUser.getRoleIds());
        sysUserPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
        insertUserPost(userId, sysUser.getPostIds());
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public void updateUserPassword(SysUser sysUser) {
        sysUserMapper.updateUserPassword(sysUser);
    }

    @Override
    public void updateUserPassword(Long userId, String oldPwd, String newPwd) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (oldPwd.equals(newPwd)) {
            throw new ServiceException("新密码与旧密码一致");
        }
        if (!SecurityUtils.matchesPassword(oldPwd, sysUser.getPassword())) {
            throw new ServiceException("旧密码错误");
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(newPwd));
        updateUserPassword(sysUser);
    }

    @Override
    public void updateUserPassword(String identify, String column, String pwd) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (column.equals("phone")) {
            wrapper.eq(ObjectUtils.isNotEmpty(identify), SysUser::getPhone, identify);
        } else if (column.equals("email")) {
            wrapper.eq(ObjectUtils.isNotEmpty(identify), SysUser::getEmail, identify);
        }
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new ServiceException("用户不存在");
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(pwd));
        updateUserPassword(sysUser);
    }


    @Override
    public void updateUserProfile(SysUser sysUser) {
        checkUpdate(sysUser);
        sysUserMapper.updateUserProfile(sysUser);
    }

    @Override
    public String updateUserAvatar(Long userId, String url) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        sysUserMapper.updateUserAvatar(userId, url);
        return sysUser.getAvatar();
    }

    @Override
    public void deleteUsers(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, list));
        sysUserPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId, list));
        sysUserMapper.deleteBatchIds(list);
    }

    @Override
    public com.huii.common.core.model.Page queryNonAuthUser(SysUser sysUser, PageParam pageParam) {
        List<Long> userIds = sysUserRoleMapper.selectUserIdsByRoleId(sysUser.getRoleId());
        if (userIds.isEmpty()) {
            userIds.add(-1L);
        }
        IPage<SysUser> iPage = new PageParamUtils<SysUser>().getPageInfo(pageParam);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.notIn(SysUser::getUserId, userIds).eq(SysUser::getUserStatus, SystemConstants.STATUS_1)
                .eq(SysUser::getDeleteFlag, SystemConstants.STATUS_0)
                .like(StringUtils.isNotBlank(sysUser.getUserName()), SysUser::getUserName, sysUser.getUserName());
        return new com.huii.common.core.model.Page(this.page(iPage, wrapper));
    }

    @Override
    public com.huii.common.core.model.Page queryAuthUser(SysUser sysUser, PageParam pageParam) {
        List<Long> userIds = sysUserRoleMapper.selectUserIdsByRoleId(sysUser.getRoleId());
        if (userIds.isEmpty()) {
            //添加-1，防止userIds为空导致报错
            userIds.add(-1L);
        }
        IPage<SysUser> iPage = new PageParamUtils<SysUser>().getPageInfo(pageParam);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIds)
                .like(StringUtils.isNotBlank(sysUser.getUserName()), SysUser::getUserName, sysUser.getUserName());
        return new com.huii.common.core.model.Page(this.page(iPage, wrapper));
    }

    @Override
    public void authUser(Long roleId, Long[] userIds) {
        if (userIds.length == 0) {
            return;
        }
        List<SysUserRole> list = new ArrayList<>(userIds.length);
        for (Long userId : userIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        sysUserRoleMapper.insertBatch(list);
    }

    @Override
    public void unauthUser(Long roleId, Long[] userIds) {
        if (userIds.length == 0) {
            return;
        }
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, Arrays.asList(userIds)));
    }

    @Override
    public com.huii.common.core.model.Page queryUserSubscribe(SysUser sysUser, PageParam pageParam, List<Long> userIds) {
        if (ObjectUtils.isEmpty(userIds)) {
            userIds.add(-1L);
        }
        IPage<SysUser> iPage = new PageParamUtils<SysUser>().getPageInfo(pageParam);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIds)
                .eq(SysUser::getDeleteFlag, SystemConstants.STATUS_0)
                .like(StringUtils.isNotBlank(sysUser.getUserName()), SysUser::getUserName, sysUser.getUserName());
        return new com.huii.common.core.model.Page(this.page(iPage, wrapper));
    }

    @Override
    public Map<String, String> queryUserBindPhoneOrEmail() {
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getUserId());
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new RuntimeException("用户信息获取失败");
        }
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(sysUser.getPhone())) {
            map.put("phone", "未绑定");
            map.put("phoneStatus", "0");
        } else {
            map.put("phone", maskPhoneNumber(sysUser.getPhone()));
            map.put("phoneStatus", "1");
        }
        if (StringUtils.isBlank(sysUser.getEmail())) {
            map.put("email", "未绑定");
            map.put("emailStatus", "0");
        } else {
            map.put("email", maskEmail(sysUser.getEmail()));
            map.put("emailStatus", "1");
        }
        return map;
    }

    @Override
    public void checkUserEmail(String email, Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (!sysUser.getEmail().equals(email)) {
            throw new ServiceException("您提供的邮箱不正确！");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void bindEmail(Long uid, String email) {
        SysUser sysUser = sysUserMapper.selectById(uid);
        sysUser.setEmail(email);
        sysUserMapper.updateById(sysUser);
    }

    private Wrapper<SysUser> wrapperBuilder(SysUser user) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        Map<String, Object> params = user.getParams();
        wrapper.eq("u.delete_flag", SystemConstants.STATUS_0)
                .eq(ObjectUtils.isNotEmpty(user.getUserId()), "u.user_id", user.getUserId())
                .eq(ObjectUtils.isNotEmpty(user.getUserStatus()), "u.user_status", user.getUserStatus())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .like(StringUtils.isNotBlank(user.getPhone()), "u.phone", user.getPhone())
                .like(StringUtils.isNotBlank(user.getEmail()), "u.email", user.getEmail())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) && ObjectUtils.isNotEmpty(params.get("endTime")),
                        "u.create_time", TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .and(ObjectUtils.isNotEmpty(user.getDeptId()), i -> {
                    List<Long> longs = sysDeptMapper.selectDeptChildIdsByParentId(user.getDeptId());
                    i.in("u.dept_id", longs);
                });
        return wrapper;
    }

    private void insertUserRole(Long userId, Long[] roleIds) {
        List<SysUserRole> list = new ArrayList<>(roleIds.length);
        for (Long roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        sysUserRoleMapper.insertBatch(list);
    }


    private void insertUserPost(Long userId, Long[] postIds) {
        List<SysUserPost> list = new ArrayList<>(postIds.length);
        for (Long postId : postIds) {
            SysUserPost sysUserPost = new SysUserPost();
            sysUserPost.setUserId(userId);
            sysUserPost.setPostId(postId);
            list.add(sysUserPost);
        }
        sysUserPostMapper.insertBatch(list);
    }

    private static String maskEmail(String email) {
        int atIndex = email.indexOf('@');

        if (atIndex > 1) {
            String prefix = email.substring(0, 1);
            String suffix = email.substring(atIndex - 1);
            String maskedMiddle = "*".repeat(atIndex - 2);
            return prefix + maskedMiddle + suffix;
        } else {
            return email;
        }
    }

    private static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 3) {
            return phoneNumber;
        }
        if (phoneNumber.length() < 11) {
            phoneNumber = phoneNumber.substring(0, 3) + "******";
        } else {
            phoneNumber = phoneNumber.substring(0, 3) + "******" + phoneNumber.substring(7);
        }
        return phoneNumber;
    }

}

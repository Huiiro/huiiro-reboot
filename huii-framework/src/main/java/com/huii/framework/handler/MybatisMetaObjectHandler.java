package com.huii.framework.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.huii.common.core.model.LoginUser;
import com.huii.common.core.model.base.BaseEntity;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * mybatisPlus 数据填充
 *
 * @author huii
 */
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtils.isNotEmpty(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                LocalDateTime create = ObjectUtils.isNotEmpty(baseEntity.getCreateTime())
                        ? baseEntity.getCreateTime() : LocalDateTime.now();
                baseEntity.setCreateTime(create);
                baseEntity.setUpdateTime(create);

                String username = StringUtils.isNotBlank(baseEntity.getCreateBy())
                        ? baseEntity.getCreateBy() : getLoginUsername();
                baseEntity.setCreateBy(username);
                baseEntity.setUpdateBy(username);
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "自动注入异常");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtils.isNotEmpty(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                baseEntity.setUpdateTime(LocalDateTime.now());

                String username = getLoginUsername();
                if (StringUtils.isNotBlank(username)) {
                    baseEntity.setUpdateBy(username);
                }
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "自动注入异常");
        }
    }

    /**
     * 获取登录用户名
     */
    private String getLoginUsername() {
        LoginUser loginUser;
        try {
            loginUser = SecurityUtils.getPrincipal();
        } catch (Exception e) {
            return null;
        }
        return ObjectUtil.isNotNull(loginUser) ? SecurityUtils.getUsername() : null;
    }
}
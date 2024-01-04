package com.huii.framework.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.huii.common.core.model.LoginUser;
import com.huii.common.core.model.base.BaseEntity;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

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
            if (ObjectUtils.isNotEmpty(metaObject) &&
                    metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
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
            ResType resType = ResType.SYS_AUTO_INJECT_ERROR;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtils.isNotEmpty(metaObject) &&
                    metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                baseEntity.setUpdateTime(LocalDateTime.now());

                String username = getLoginUsername();
                if (StringUtils.isNotBlank(username)) {
                    baseEntity.setUpdateBy(username);
                }
            }
        } catch (Exception e) {
            ResType resType = ResType.SYS_AUTO_INJECT_ERROR;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
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
package com.huii.common.core.model.base;


import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;

/**
 * 控制层基类
 *
 * @author huii
 */
public class BaseController {

    public boolean isAdmin() {
        return SecurityUtils.isAdmin();
    }

    public Long getUserId() {
        return SecurityUtils.getUserId();
    }

    public Long safeGetUserId() {
        return SecurityUtils.safeGetUserId();
    }

    public <T> R<T> saveSuccess() {
        return saveSuccess(null);
    }

    public <T> R<T> updateSuccess() {
        return updateSuccess(null);
    }

    public <T> R<T> deleteSuccess() {
        return deleteSuccess(null);
    }

    public <T> R<T> sendSuccess() {
        return sendSuccess(null);
    }

    public <T> R<T> saveSuccess(T data) {
        ResType res = ResType.COMMON_INSERT_SUCCESS;
        return R.ok(res.getCode(), MessageUtils.message(res.getI18n()), data);
    }

    public <T> R<T> updateSuccess(T data) {
        ResType res = ResType.COMMON_UPDATE_SUCCESS;
        return R.ok(res.getCode(), MessageUtils.message(res.getI18n()), data);
    }

    public <T> R<T> deleteSuccess(T data) {
        ResType res = ResType.COMMON_DELETE_SUCCESS;
        return R.ok(res.getCode(), MessageUtils.message(res.getI18n()), data);
    }

    public <T> R<T> sendSuccess(T data) {
        ResType res = ResType.USER_CAPTCHA_SEND_SUCCESS;
        return R.ok(res.getCode(), MessageUtils.message(res.getI18n()), data);
    }
}

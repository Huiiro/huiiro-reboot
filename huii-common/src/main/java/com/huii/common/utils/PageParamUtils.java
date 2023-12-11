package com.huii.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huii.common.core.model.PageParam;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 分页参数工具
 *
 * @author huii
 */
public class PageParamUtils<T> {

    private static final String CURRENT = "current";

    private static final String SIZE = "size";

    public IPage<T> getPageInfo(Map<String, Object> params) {
        return getPage(params);
    }

    public IPage<T> getPageInfo(String currentParam, String sizeParam) {
        return getPage(currentParam, sizeParam);
    }

    public IPage<T> getPageInfo(PageParam pageParam) {
        return getPage(pageParam);
    }

    public Page<T> getPage(Map<String, Object> params) {
        int current = 1;
        int pageSize = 10;
        if (params.get(CURRENT) != null) {
            current = Integer.parseInt((String) params.get(CURRENT));
        }
        if (params.get(SIZE) != null) {
            pageSize = Integer.parseInt((String) params.get(SIZE));
        }

        return new Page<>(current, pageSize);
    }

    public Page<T> getPage(String currentParam, String sizeParam) {
        int current = 1;
        int pageSize = 10;
        if (StringUtils.isNotEmpty(currentParam)) {
            current = Integer.parseInt(currentParam);
        }
        if (StringUtils.isNotEmpty(sizeParam)) {
            pageSize = Integer.parseInt(sizeParam);
        }

        return new Page<>(current, pageSize);
    }

    public Page<T> getPage(PageParam pageParam) {
        int current = 1;
        int pageSize = 10;
        if (ObjectUtils.isNotEmpty(pageParam.getCurrent())) {
            current = pageParam.getCurrent();
        }
        if (ObjectUtils.isNotEmpty(pageParam.getSize())) {
            pageSize = pageParam.getSize();
        }

        return new Page<>(current, pageSize);
    }
}

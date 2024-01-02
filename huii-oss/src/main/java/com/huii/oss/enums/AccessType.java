package com.huii.oss.enums;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 桶访问策略配置
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum AccessType {
    /**
     * private
     */
    PRIVATE("0", "write-only", CannedAccessControlList.Private),

    /**
     * public
     */
    PUBLIC("1", "read-only", CannedAccessControlList.PublicRead),

    /**
     * custom
     */
    CUSTOM("2", "read-only", CannedAccessControlList.PublicRead);

    /**
     * 桶 权限类型
     */
    private final String type;

    /**
     * 桶策略类型
     */
    private final String policyType;

    /**
     * 文件对象 权限类型
     */
    private final CannedAccessControlList acl;

    public static AccessType getType(String type) {
        for (AccessType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        throw new RuntimeException("'type' not found By " + type);
    }
}

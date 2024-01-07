package com.huii.message.mapper;

import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.message.domain.MsgMailTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件模板数据层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Mapper
public interface MsgMailTemplateMapper extends BaseMapperPlus<MsgMailTemplate> {

    /**
     * 获取模板信息
     *
     * @param template templateName
     * @return template
     */
    MsgMailTemplate loadTemplate(String template);
}
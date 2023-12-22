package ${packageName}.mapper;

import com.huii.common.core.model.base.BaseMapperPlus;
import ${packageName}.domain.${className};
import org.apache.ibatis.annotations.Mapper;

/**
 * <#if moduleFunctionDesc?has_content>${moduleFunctionDesc}</#if>数据层接口
 *
 * @author ${authorName}
 * @date ${createTime}
 */
@Mapper
public interface ${className}Mapper extends BaseMapperPlus<${className}> {
}
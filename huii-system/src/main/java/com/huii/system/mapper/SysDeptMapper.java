package com.huii.system.mapper;

import com.huii.common.core.domain.SysDept;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapperPlus<SysDept> {

    /**
     * 递归查询部门下所有子部门ID
     * <p>pgSQL</p>
     *
     * @param id id
     * @return ids
     */
    List<Long> selectDeptChildIdsByParentId(Long id);
}

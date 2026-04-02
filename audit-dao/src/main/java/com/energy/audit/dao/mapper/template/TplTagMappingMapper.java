package com.energy.audit.dao.mapper.template;

import com.energy.audit.model.entity.template.TplTagMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper for tpl_tag_mapping
 */
@Mapper
public interface TplTagMappingMapper {

    int batchInsert(@Param("list") List<TplTagMapping> list);

    List<TplTagMapping> selectListByVersionId(@Param("templateVersionId") Long templateVersionId);

    TplTagMapping selectById(@Param("id") Long id);

    int updateById(TplTagMapping mapping);

    /** Soft-delete all mappings for a version (used before replacing with new batch) */
    int deleteByVersionId(@Param("templateVersionId") Long templateVersionId,
                          @Param("updateBy") String updateBy);
}

package com.energy.audit.dao.mapper.template;

import com.energy.audit.model.entity.template.TplEditLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper for tpl_edit_lock (pessimistic lock for concurrent editing prevention)
 */
@Mapper
public interface TplEditLockMapper {

    /**
     * Insert a new lock row. Uses ON DUPLICATE KEY UPDATE on the unique index
     * (enterprise_id, template_id, audit_year) to act as an upsert.
     */
    int insertOrUpdate(TplEditLock lock);

    TplEditLock selectByKey(@Param("enterpriseId") Long enterpriseId,
                            @Param("templateId") Long templateId,
                            @Param("auditYear") Integer auditYear);

    int deleteByKey(@Param("enterpriseId") Long enterpriseId,
                    @Param("templateId") Long templateId,
                    @Param("auditYear") Integer auditYear,
                    @Param("updateBy") String updateBy);
}

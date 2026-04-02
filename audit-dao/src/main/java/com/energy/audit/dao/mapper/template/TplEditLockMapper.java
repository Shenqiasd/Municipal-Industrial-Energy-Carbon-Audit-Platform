package com.energy.audit.dao.mapper.template;

import com.energy.audit.model.entity.template.TplEditLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * Mapper for tpl_edit_lock (pessimistic lock for concurrent editing prevention).
 *
 * acquireLock flow (must be called inside @Transactional):
 *   1. selectByKeyForUpdate  — row-level FOR UPDATE lock, no deleted filter
 *   2. If null → insert;  otherwise → updateByKey (refreshes / reactivates)
 *
 * checkLock / releaseLock use selectByKey (deleted=0 only, no FOR UPDATE).
 */
@Mapper
public interface TplEditLockMapper {

    /**
     * Select lock row for any deleted state. Uses SELECT ... FOR UPDATE to prevent
     * concurrent inserts/updates to the same key. Must be called within @Transactional.
     */
    TplEditLock selectByKeyForUpdate(@Param("enterpriseId") Long enterpriseId,
                                     @Param("templateId") Long templateId,
                                     @Param("auditYear") Integer auditYear);

    /**
     * Select active (deleted=0) lock row. Used by checkLock and releaseLock.
     */
    TplEditLock selectByKey(@Param("enterpriseId") Long enterpriseId,
                            @Param("templateId") Long templateId,
                            @Param("auditYear") Integer auditYear);

    /**
     * Insert a brand-new lock row (no existing row for this key).
     */
    int insert(TplEditLock lock);

    /**
     * Update an existing lock row by composite key. Resets deleted=0,
     * updates lockUserId / lockTime / expireTime / updateBy.
     */
    int updateByKey(@Param("enterpriseId") Long enterpriseId,
                    @Param("templateId") Long templateId,
                    @Param("auditYear") Integer auditYear,
                    @Param("lockUserId") Long lockUserId,
                    @Param("lockTime") LocalDateTime lockTime,
                    @Param("expireTime") LocalDateTime expireTime,
                    @Param("updateBy") String updateBy);

    int deleteByKey(@Param("enterpriseId") Long enterpriseId,
                    @Param("templateId") Long templateId,
                    @Param("auditYear") Integer auditYear,
                    @Param("updateBy") String updateBy);
}

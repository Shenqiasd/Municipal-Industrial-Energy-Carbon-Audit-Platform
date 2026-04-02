package com.energy.audit.service.template;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.template.TplEditLockMapper;
import com.energy.audit.model.entity.template.TplEditLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service for managing template edit locks (pessimistic locking).
 * Backed by tpl_edit_lock table — locks survive server restarts.
 */
@Service
public class EditLockService {

    private static final Logger log = LoggerFactory.getLogger(EditLockService.class);

    private static final int LOCK_TIMEOUT_MINUTES = 30;

    private final TplEditLockMapper editLockMapper;

    public EditLockService(TplEditLockMapper editLockMapper) {
        this.editLockMapper = editLockMapper;
    }

    /**
     * Acquire an edit lock for a template/enterprise/year triple.
     * If a non-expired lock held by another user exists, throws BusinessException.
     * If the current user already holds the lock, the expiry is refreshed.
     */
    public TplEditLock acquireLock(Long enterpriseId, Long templateId, Integer auditYear) {
        Long currentUserId = SecurityUtils.getRequiredCurrentUserId();
        String operator = SecurityUtils.getCurrentUsername();

        TplEditLock existing = editLockMapper.selectByKey(enterpriseId, templateId, auditYear);
        if (existing != null && existing.getExpireTime().isAfter(LocalDateTime.now())) {
            if (!existing.getLockUserId().equals(currentUserId)) {
                throw new BusinessException("当前文档正在被其他用户编辑中");
            }
        }

        TplEditLock lock = new TplEditLock();
        lock.setEnterpriseId(enterpriseId);
        lock.setTemplateId(templateId);
        lock.setAuditYear(auditYear);
        lock.setLockUserId(currentUserId);
        lock.setLockTime(LocalDateTime.now());
        lock.setExpireTime(LocalDateTime.now().plusMinutes(LOCK_TIMEOUT_MINUTES));
        lock.setCreateBy(operator);
        lock.setUpdateBy(operator);
        editLockMapper.insertOrUpdate(lock);

        log.info("Lock acquired: enterprise={} template={} year={} user={}",
                enterpriseId, templateId, auditYear, currentUserId);
        return lock;
    }

    /**
     * Release a lock. Only the lock owner may release their own lock.
     */
    public void releaseLock(Long enterpriseId, Long templateId, Integer auditYear) {
        Long currentUserId = SecurityUtils.getRequiredCurrentUserId();
        String operator = SecurityUtils.getCurrentUsername();

        TplEditLock existing = editLockMapper.selectByKey(enterpriseId, templateId, auditYear);
        if (existing != null) {
            if (!existing.getLockUserId().equals(currentUserId)) {
                throw new BusinessException("无法释放其他用户持有的编辑锁");
            }
            editLockMapper.deleteByKey(enterpriseId, templateId, auditYear, operator);
            log.info("Lock released: enterprise={} template={} year={}",
                    enterpriseId, templateId, auditYear);
        }
    }

    /**
     * Check lock status. Returns null if no active (non-expired) lock exists.
     */
    public TplEditLock checkLock(Long enterpriseId, Long templateId, Integer auditYear) {
        TplEditLock lock = editLockMapper.selectByKey(enterpriseId, templateId, auditYear);
        if (lock != null && lock.getExpireTime().isBefore(LocalDateTime.now())) {
            editLockMapper.deleteByKey(enterpriseId, templateId, auditYear,
                    SecurityUtils.getCurrentUsername());
            return null;
        }
        return lock;
    }
}

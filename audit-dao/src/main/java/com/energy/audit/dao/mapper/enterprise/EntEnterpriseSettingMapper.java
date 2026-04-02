package com.energy.audit.dao.mapper.enterprise;

import com.energy.audit.model.entity.enterprise.EntEnterpriseSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Enterprise setting mapper — one row per enterprise (upsert pattern).
 */
@Mapper
public interface EntEnterpriseSettingMapper {

    EntEnterpriseSetting selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    int upsert(EntEnterpriseSetting setting);
}

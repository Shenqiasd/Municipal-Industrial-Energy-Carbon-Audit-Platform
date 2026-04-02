package com.energy.audit.dao.mapper.config;

import com.energy.audit.model.entity.config.CmEmissionFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Carbon emission factor mapper
 */
@Mapper
public interface CmEmissionFactorMapper {

    CmEmissionFactor selectById(@Param("id") Long id);

    List<CmEmissionFactor> selectList(CmEmissionFactor query);

    int insert(CmEmissionFactor factor);

    int updateById(CmEmissionFactor factor);

    int deleteById(@Param("id") Long id, @Param("updateBy") String updateBy);
}

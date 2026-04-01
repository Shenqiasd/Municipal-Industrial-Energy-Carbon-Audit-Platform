package com.energy.audit.dao.mapper.system;

import com.energy.audit.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * System user mapper
 */
@Mapper
public interface SysUserMapper {

    SysUser selectById(@Param("id") Long id);

    SysUser selectByUsername(@Param("username") String username);

    List<SysUser> selectList(SysUser query);

    int insert(SysUser user);

    int updateById(SysUser user);

    int deleteById(@Param("id") Long id);
}

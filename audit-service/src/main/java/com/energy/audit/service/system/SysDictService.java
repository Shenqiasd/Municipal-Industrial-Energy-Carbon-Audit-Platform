package com.energy.audit.service.system;

import com.energy.audit.model.entity.system.SysDictData;
import com.energy.audit.model.entity.system.SysDictType;

import java.util.List;
import java.util.Map;

/**
 * System dictionary service
 */
public interface SysDictService {

    // ---- Dict Type ----
    SysDictType getTypeById(Long id);

    List<SysDictType> listTypes(SysDictType query);

    void createType(SysDictType dictType);

    void updateType(SysDictType dictType);

    void deleteType(Long id);

    // ---- Dict Data ----
    SysDictData getDataById(Long id);

    List<SysDictData> listData(SysDictData query);

    List<SysDictData> getDataByType(String dictType);

    Map<String, List<SysDictData>> getDataByTypes(List<String> dictTypes);

    void createData(SysDictData dictData);

    void updateData(SysDictData dictData);

    void deleteData(Long id);
}

package com.energy.audit.service.audit;

import com.energy.audit.model.entity.audit.AwRectificationTrack;

import java.util.List;

public interface RectificationService {

    void createItems(Long taskId, List<AwRectificationTrack> items, String username);

    List<AwRectificationTrack> listByTaskId(Long taskId);

    List<AwRectificationTrack> listByEnterpriseId(Long enterpriseId);

    int countOverdueByTaskId(Long taskId);

    void updateProgress(Long id, Integer status, String result, String username);

    void acceptItem(Long id, String username);

    AwRectificationTrack getById(Long id);

    void markOverdueItems();
}

package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.RiskAlert;

import java.util.List;

public interface RiskAlertService extends IService<RiskAlert> {

    RiskAlert createAlert(RiskAlert alert);

    boolean cancelAlert(Long alertId, Long userId);

    boolean handleAlert(Long alertId, Long handlerId, String result);

    List<RiskAlert> getAlertsByUser(Long userId);

    PageResult<RiskAlert> getAlertPage(Long userId, Integer riskLevel, Integer handleStatus, Integer page, Integer size);

    List<RiskAlert> getUnhandledAlerts(Long communityId);

    long countTodayAlerts(Long communityId);

    List<RiskAlert> getRecentAlerts(Long userId, int limit);
}

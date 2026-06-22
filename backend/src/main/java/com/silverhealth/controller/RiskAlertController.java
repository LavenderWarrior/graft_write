package com.silverhealth.controller;

import com.silverhealth.common.PageResult;
import com.silverhealth.common.Result;
import com.silverhealth.entity.RiskAlert;
import com.silverhealth.service.RiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class RiskAlertController {

    @Autowired
    private RiskAlertService alertService;

    @PostMapping("/create")
    public Result<RiskAlert> create(@RequestBody RiskAlert alert) {
        return Result.success(alertService.createAlert(alert));
    }

    @PostMapping("/cancel/{alertId}")
    public Result<Void> cancel(@PathVariable Long alertId, @RequestParam Long userId) {
        alertService.cancelAlert(alertId, userId);
        return Result.success();
    }

    @PostMapping("/handle/{alertId}")
    public Result<Void> handle(@PathVariable Long alertId,
                                @RequestParam Long handlerId,
                                @RequestParam String result) {
        alertService.handleAlert(alertId, handlerId, result);
        return Result.success();
    }

    @GetMapping("/user/{userId}")
    public Result<List<RiskAlert>> userAlerts(@PathVariable Long userId) {
        return Result.success(alertService.getAlertsByUser(userId));
    }

    @GetMapping("/page")
    public Result<PageResult<RiskAlert>> alertPage(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer riskLevel,
            @RequestParam(required = false) Integer handleStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(alertService.getAlertPage(userId, riskLevel, handleStatus, page, size));
    }

    @GetMapping("/unhandled/{communityId}")
    public Result<List<RiskAlert>> unhandled(@PathVariable Long communityId) {
        return Result.success(alertService.getUnhandledAlerts(communityId));
    }

    @GetMapping("/today-count/{communityId}")
    public Result<Long> todayCount(@PathVariable Long communityId) {
        return Result.success(alertService.countTodayAlerts(communityId));
    }

    @GetMapping("/recent/{userId}")
    public Result<List<RiskAlert>> recent(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "5") int limit) {
        return Result.success(alertService.getRecentAlerts(userId, limit));
    }
}

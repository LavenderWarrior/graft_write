package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.HealthDailyReport;
import com.silverhealth.entity.HealthPeriodReport;
import com.silverhealth.service.HealthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/health-report")
public class HealthReportController {

    @Autowired
    private HealthReportService healthReportService;

    @GetMapping("/today/{userId}")
    public Result<HealthDailyReport> todayReport(@PathVariable Long userId) {
        return Result.success(healthReportService.getTodayReport(userId));
    }

    @GetMapping("/daily/{userId}")
    public Result<List<HealthDailyReport>> dailyReports(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(healthReportService.getDailyReports(userId, start, end));
    }

    @GetMapping("/weekly/{userId}")
    public Result<HealthPeriodReport> weeklyReport(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate weekStart) {
        return Result.success(healthReportService.getWeeklyReport(userId, weekStart));
    }

    @GetMapping("/monthly/{userId}")
    public Result<HealthPeriodReport> monthlyReport(
            @PathVariable Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return Result.success(healthReportService.getMonthlyReport(userId, year, month));
    }

    @GetMapping("/recent/{userId}")
    public Result<List<HealthDailyReport>> recentReports(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        return Result.success(healthReportService.getRecentDays(userId, days));
    }
}

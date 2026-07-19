package com.silverhealth.service;

import com.silverhealth.entity.HealthDailyReport;
import com.silverhealth.entity.HealthPeriodReport;

import java.time.LocalDate;
import java.util.List;

public interface HealthReportService {

    HealthDailyReport getTodayReport(Long userId);

    List<HealthDailyReport> getDailyReports(Long userId, LocalDate start, LocalDate end);

    HealthPeriodReport getWeeklyReport(Long userId, LocalDate weekStart);

    HealthPeriodReport getMonthlyReport(Long userId, int year, int month);

    List<HealthDailyReport> getRecentDays(Long userId, int days);
}

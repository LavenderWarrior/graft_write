package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silverhealth.entity.HealthDailyReport;
import com.silverhealth.entity.HealthPeriodReport;
import com.silverhealth.mapper.HealthDailyReportMapper;
import com.silverhealth.mapper.HealthPeriodReportMapper;
import com.silverhealth.service.HealthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HealthReportServiceImpl implements HealthReportService {

    @Autowired
    private HealthDailyReportMapper dailyReportMapper;

    @Autowired
    private HealthPeriodReportMapper periodReportMapper;

    @Override
    public HealthDailyReport getTodayReport(Long userId) {
        LambdaQueryWrapper<HealthDailyReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthDailyReport::getUserId, userId)
                .eq(HealthDailyReport::getReportDate, LocalDate.now());
        return dailyReportMapper.selectOne(wrapper);
    }

    @Override
    public List<HealthDailyReport> getDailyReports(Long userId, LocalDate start, LocalDate end) {
        LambdaQueryWrapper<HealthDailyReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthDailyReport::getUserId, userId)
                .ge(HealthDailyReport::getReportDate, start)
                .le(HealthDailyReport::getReportDate, end)
                .orderByDesc(HealthDailyReport::getReportDate);
        return dailyReportMapper.selectList(wrapper);
    }

    @Override
    public HealthPeriodReport getWeeklyReport(Long userId, LocalDate weekStart) {
        LambdaQueryWrapper<HealthPeriodReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthPeriodReport::getUserId, userId)
                .eq(HealthPeriodReport::getReportType, 1)
                .eq(HealthPeriodReport::getStartDate, weekStart);
        return periodReportMapper.selectOne(wrapper);
    }

    @Override
    public HealthPeriodReport getMonthlyReport(Long userId, int year, int month) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LambdaQueryWrapper<HealthPeriodReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthPeriodReport::getUserId, userId)
                .eq(HealthPeriodReport::getReportType, 2)
                .eq(HealthPeriodReport::getStartDate, monthStart);
        return periodReportMapper.selectOne(wrapper);
    }

    @Override
    public List<HealthDailyReport> getRecentDays(Long userId, int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days);
        return getDailyReports(userId, start, end);
    }
}

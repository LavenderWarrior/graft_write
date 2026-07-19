package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.RiskAlert;
import com.silverhealth.entity.SysUser;
import com.silverhealth.mapper.RiskAlertMapper;
import com.silverhealth.mapper.SysUserMapper;
import com.silverhealth.service.RiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskAlertServiceImpl extends ServiceImpl<RiskAlertMapper, RiskAlert> implements RiskAlertService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public RiskAlert createAlert(RiskAlert alert) {
        alert.setHandleStatus(0);
        save(alert);
        return alert;
    }

    @Override
    public boolean cancelAlert(Long alertId, Long userId) {
        RiskAlert alert = getById(alertId);
        if (alert == null || !alert.getUserId().equals(userId)) {
            return false;
        }
        alert.setHandleStatus(2);
        alert.setHandleResult("老人自行取消告警");
        alert.setHandleTime(LocalDateTime.now());
        return updateById(alert);
    }

    @Override
    public boolean handleAlert(Long alertId, Long handlerId, String result) {
        RiskAlert alert = getById(alertId);
        if (alert == null) {
            return false;
        }
        alert.setHandleStatus(1);
        alert.setHandleResult(result);
        alert.setHandleTime(LocalDateTime.now());
        alert.setHandlerId(handlerId);
        return updateById(alert);
    }

    @Override
    public List<RiskAlert> getAlertsByUser(Long userId) {
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RiskAlert::getUserId, userId).orderByDesc(RiskAlert::getAlertTime);
        return list(wrapper);
    }

    @Override
    public PageResult<RiskAlert> getAlertPage(Long userId, Integer riskLevel, Integer handleStatus,
                                               Integer page, Integer size) {
        Page<RiskAlert> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(RiskAlert::getUserId, userId);
        }
        if (riskLevel != null) {
            wrapper.eq(RiskAlert::getRiskLevel, riskLevel);
        }
        if (handleStatus != null) {
            wrapper.eq(RiskAlert::getHandleStatus, handleStatus);
        }
        wrapper.orderByDesc(RiskAlert::getAlertTime);
        Page<RiskAlert> result = page(pageParam, wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }

    @Override
    public List<RiskAlert> getUnhandledAlerts(Long communityId) {
        LambdaQueryWrapper<SysUser> uw = new LambdaQueryWrapper<>();
        uw.eq(SysUser::getUserType, 1).eq(SysUser::getCommunityId, communityId);
        List<SysUser> elders = sysUserMapper.selectList(uw);
        List<Long> elderIds = elders.stream().map(SysUser::getId).collect(Collectors.toList());
        if (elderIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RiskAlert::getUserId, elderIds)
                .eq(RiskAlert::getHandleStatus, 0)
                .orderByDesc(RiskAlert::getRiskLevel)
                .orderByDesc(RiskAlert::getAlertTime);
        return list(wrapper);
    }

    @Override
    public long countTodayAlerts(Long communityId) {
        LambdaQueryWrapper<SysUser> uw = new LambdaQueryWrapper<>();
        uw.eq(SysUser::getUserType, 1).eq(SysUser::getCommunityId, communityId);
        List<SysUser> elders = sysUserMapper.selectList(uw);
        List<Long> elderIds = elders.stream().map(SysUser::getId).collect(Collectors.toList());
        if (elderIds.isEmpty()) {
            return 0;
        }
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RiskAlert::getUserId, elderIds).ge(RiskAlert::getAlertTime, todayStart);
        return count(wrapper);
    }

    @Override
    public List<RiskAlert> getRecentAlerts(Long userId, int limit) {
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RiskAlert::getUserId, userId)
                .orderByDesc(RiskAlert::getAlertTime)
                .last("LIMIT " + limit);
        return list(wrapper);
    }
}

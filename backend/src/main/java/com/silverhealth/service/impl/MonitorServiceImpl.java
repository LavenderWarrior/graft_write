package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silverhealth.entity.MonitorSession;
import com.silverhealth.mapper.MonitorSessionMapper;
import com.silverhealth.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private MonitorSessionMapper monitorSessionMapper;

    @Override
    public MonitorSession startMonitor(Long userId) {
        MonitorSession existing = getCurrentSession(userId);
        if (existing != null) {
            return existing;
        }
        MonitorSession session = new MonitorSession();
        session.setUserId(userId);
        session.setSessionStart(LocalDateTime.now());
        session.setMode(1);
        session.setStatus(1);
        monitorSessionMapper.insert(session);
        return session;
    }

    @Override
    public boolean stopMonitor(Long userId) {
        MonitorSession session = getCurrentSession(userId);
        if (session == null) {
            return false;
        }
        session.setSessionEnd(LocalDateTime.now());
        session.setStatus(0);
        return monitorSessionMapper.updateById(session) > 0;
    }

    @Override
    public MonitorSession getCurrentSession(Long userId) {
        LambdaQueryWrapper<MonitorSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitorSession::getUserId, userId)
                .eq(MonitorSession::getStatus, 1)
                .orderByDesc(MonitorSession::getSessionStart)
                .last("LIMIT 1");
        return monitorSessionMapper.selectOne(wrapper);
    }

    @Override
    public boolean switchMode(Long userId, int mode) {
        MonitorSession session = getCurrentSession(userId);
        if (session == null) {
            return false;
        }
        session.setMode(mode);
        return monitorSessionMapper.updateById(session) > 0;
    }
}

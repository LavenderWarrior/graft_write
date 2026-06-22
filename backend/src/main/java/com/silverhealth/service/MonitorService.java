package com.silverhealth.service;

import com.silverhealth.entity.MonitorSession;

public interface MonitorService {

    MonitorSession startMonitor(Long userId);

    boolean stopMonitor(Long userId);

    MonitorSession getCurrentSession(Long userId);

    boolean switchMode(Long userId, int mode);
}

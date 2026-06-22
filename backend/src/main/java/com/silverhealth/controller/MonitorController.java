package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.MonitorSession;
import com.silverhealth.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @PostMapping("/start/{userId}")
    public Result<MonitorSession> start(@PathVariable Long userId) {
        return Result.success(monitorService.startMonitor(userId));
    }

    @PostMapping("/stop/{userId}")
    public Result<Void> stop(@PathVariable Long userId) {
        monitorService.stopMonitor(userId);
        return Result.success();
    }

    @GetMapping("/current/{userId}")
    public Result<MonitorSession> current(@PathVariable Long userId) {
        return Result.success(monitorService.getCurrentSession(userId));
    }

    @PostMapping("/switch-mode/{userId}")
    public Result<Void> switchMode(@PathVariable Long userId, @RequestParam int mode) {
        monitorService.switchMode(userId, mode);
        return Result.success();
    }
}

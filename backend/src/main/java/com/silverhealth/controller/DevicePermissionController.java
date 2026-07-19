package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.DevicePermission;
import com.silverhealth.service.DevicePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class DevicePermissionController {

    @Autowired
    private DevicePermissionService permissionService;

    @GetMapping("/{userId}")
    public Result<DevicePermission> getPermission(@PathVariable Long userId) {
        DevicePermission perm = permissionService.getByUserId(userId);
        if (perm == null) {
            perm = permissionService.initPermission(userId);
        }
        return Result.success(perm);
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody DevicePermission permission) {
        permissionService.updatePermission(permission);
        return Result.success();
    }

    @PostMapping("/toggle-monitor")
    public Result<Void> toggleMonitor(@RequestParam Long userId, @RequestParam boolean enable) {
        permissionService.toggleMonitor(userId, enable);
        return Result.success();
    }

    @PostMapping("/agree-privacy")
    public Result<Void> agreePrivacy(@RequestParam Long userId) {
        permissionService.agreePrivacy(userId);
        return Result.success();
    }
}

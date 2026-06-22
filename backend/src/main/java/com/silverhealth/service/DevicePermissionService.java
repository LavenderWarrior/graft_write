package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.entity.DevicePermission;

public interface DevicePermissionService extends IService<DevicePermission> {

    DevicePermission getByUserId(Long userId);

    DevicePermission initPermission(Long userId);

    boolean updatePermission(DevicePermission permission);

    boolean toggleMonitor(Long userId, boolean enable);

    boolean agreePrivacy(Long userId);
}

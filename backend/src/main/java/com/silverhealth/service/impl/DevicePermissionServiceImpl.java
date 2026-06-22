package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.entity.DevicePermission;
import com.silverhealth.mapper.DevicePermissionMapper;
import com.silverhealth.service.DevicePermissionService;
import org.springframework.stereotype.Service;

@Service
public class DevicePermissionServiceImpl extends ServiceImpl<DevicePermissionMapper, DevicePermission>
        implements DevicePermissionService {

    @Override
    public DevicePermission getByUserId(Long userId) {
        LambdaQueryWrapper<DevicePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DevicePermission::getUserId, userId);
        return getOne(wrapper);
    }

    @Override
    public DevicePermission initPermission(Long userId) {
        DevicePermission existing = getByUserId(userId);
        if (existing != null) {
            return existing;
        }
        DevicePermission perm = new DevicePermission();
        perm.setUserId(userId);
        perm.setCameraEnabled(0);
        perm.setMicEnabled(0);
        perm.setScreenOn(0);
        perm.setLowPowerMode(0);
        perm.setLocalNotify(1);
        perm.setSmsNotify(0);
        perm.setMonitorStatus(0);
        perm.setPrivacyAgreed(0);
        perm.setNoRemind(0);
        save(perm);
        return perm;
    }

    @Override
    public boolean updatePermission(DevicePermission permission) {
        return updateById(permission);
    }

    @Override
    public boolean toggleMonitor(Long userId, boolean enable) {
        DevicePermission perm = getByUserId(userId);
        if (perm == null) {
            return false;
        }
        perm.setMonitorStatus(enable ? 1 : 0);
        return updateById(perm);
    }

    @Override
    public boolean agreePrivacy(Long userId) {
        DevicePermission perm = getByUserId(userId);
        if (perm == null) {
            perm = initPermission(userId);
        }
        perm.setPrivacyAgreed(1);
        return updateById(perm);
    }
}

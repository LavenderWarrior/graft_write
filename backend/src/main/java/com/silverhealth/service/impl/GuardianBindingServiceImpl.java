package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.entity.GuardianBinding;
import com.silverhealth.mapper.GuardianBindingMapper;
import com.silverhealth.service.GuardianBindingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GuardianBindingServiceImpl extends ServiceImpl<GuardianBindingMapper, GuardianBinding>
        implements GuardianBindingService {

    @Override
    public String generateBindCode(Long elderId) {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        GuardianBinding binding = new GuardianBinding();
        binding.setElderId(elderId);
        binding.setBindCode(code);
        binding.setStatus(0);
        save(binding);
        return code;
    }

    @Override
    public GuardianBinding bindByCode(String bindCode, Long guardianId, Integer guardianType) {
        LambdaQueryWrapper<GuardianBinding> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuardianBinding::getBindCode, bindCode).eq(GuardianBinding::getStatus, 0);
        GuardianBinding binding = getOne(wrapper);
        if (binding == null) {
            return null;
        }
        binding.setGuardianId(guardianId);
        binding.setGuardianType(guardianType);
        binding.setPermission(2);
        binding.setStatus(1);
        updateById(binding);
        return binding;
    }

    @Override
    public GuardianBinding bindByWechatId(Long elderId, Long guardianId, Integer guardianType) {
        GuardianBinding binding = new GuardianBinding();
        binding.setElderId(elderId);
        binding.setGuardianId(guardianId);
        binding.setGuardianType(guardianType);
        binding.setPermission(2);
        binding.setStatus(1);
        save(binding);
        return binding;
    }

    @Override
    public boolean unbind(Long bindingId) {
        GuardianBinding binding = getById(bindingId);
        if (binding == null) {
            return false;
        }
        binding.setStatus(0);
        return updateById(binding);
    }

    @Override
    public List<GuardianBinding> getBindingsByElder(Long elderId) {
        LambdaQueryWrapper<GuardianBinding> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuardianBinding::getElderId, elderId).eq(GuardianBinding::getStatus, 1);
        return list(wrapper);
    }

    @Override
    public List<GuardianBinding> getBindingsByGuardian(Long guardianId) {
        LambdaQueryWrapper<GuardianBinding> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuardianBinding::getGuardianId, guardianId).eq(GuardianBinding::getStatus, 1);
        return list(wrapper);
    }
}

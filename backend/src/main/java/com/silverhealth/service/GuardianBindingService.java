package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.entity.GuardianBinding;

import java.util.List;

public interface GuardianBindingService extends IService<GuardianBinding> {

    String generateBindCode(Long elderId);

    GuardianBinding bindByCode(String bindCode, Long guardianId, Integer guardianType);

    GuardianBinding bindByWechatId(Long elderId, Long guardianId, Integer guardianType);

    boolean unbind(Long bindingId);

    List<GuardianBinding> getBindingsByElder(Long elderId);

    List<GuardianBinding> getBindingsByGuardian(Long guardianId);
}

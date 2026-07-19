package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.entity.Membership;

public interface MembershipService extends IService<Membership> {

    Membership getCurrentMembership(Long userId);

    Membership subscribe(Long userId, Integer planType);

    boolean isVip(Long userId);
}

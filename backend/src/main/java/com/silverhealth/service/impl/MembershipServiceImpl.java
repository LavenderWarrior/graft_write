package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.entity.Membership;
import com.silverhealth.mapper.MembershipMapper;
import com.silverhealth.service.MembershipService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, Membership> implements MembershipService {

    @Override
    public Membership getCurrentMembership(Long userId) {
        LambdaQueryWrapper<Membership> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Membership::getUserId, userId)
                .eq(Membership::getStatus, 1)
                .orderByDesc(Membership::getEndDate)
                .last("LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public Membership subscribe(Long userId, Integer planType) {
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setPlanType(planType);
        membership.setStartDate(LocalDate.now());
        membership.setStatus(1);

        if (planType == 1) {
            membership.setPrice(new BigDecimal("19.00"));
            membership.setEndDate(LocalDate.now().plusMonths(1));
        } else if (planType == 2) {
            membership.setPrice(new BigDecimal("168.00"));
            membership.setEndDate(LocalDate.now().plusYears(1));
        } else {
            membership.setPrice(BigDecimal.ZERO);
            membership.setEndDate(LocalDate.of(2099, 12, 31));
        }

        save(membership);
        return membership;
    }

    @Override
    public boolean isVip(Long userId) {
        Membership current = getCurrentMembership(userId);
        return current != null && current.getPlanType() > 0 && current.getEndDate().isAfter(LocalDate.now());
    }
}

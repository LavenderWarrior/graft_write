package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.Membership;
import com.silverhealth.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/current/{userId}")
    public Result<Membership> current(@PathVariable Long userId) {
        return Result.success(membershipService.getCurrentMembership(userId));
    }

    @PostMapping("/subscribe")
    public Result<Membership> subscribe(@RequestParam Long userId, @RequestParam Integer planType) {
        return Result.success(membershipService.subscribe(userId, planType));
    }

    @GetMapping("/is-vip/{userId}")
    public Result<Boolean> isVip(@PathVariable Long userId) {
        return Result.success(membershipService.isVip(userId));
    }
}

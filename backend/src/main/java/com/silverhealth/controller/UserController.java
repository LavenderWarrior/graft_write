package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.SysUser;
import com.silverhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestParam String openid, @RequestParam Integer userType) {
        SysUser user = userService.loginByOpenid(openid, userType);
        if (user == null) {
            return Result.error(404, "用户不存在，请先注册");
        }
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        return Result.success(userService.register(user));
    }

    @GetMapping("/detail/{id}")
    public Result<SysUser> detail(@PathVariable Long id) {
        return Result.success(userService.getUserDetail(id));
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody SysUser user) {
        userService.updateById(user);
        return Result.success();
    }

    @GetMapping("/bound-elders/{guardianId}")
    public Result<List<SysUser>> boundElders(@PathVariable Long guardianId) {
        return Result.success(userService.getBoundElders(guardianId));
    }
}

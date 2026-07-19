package com.silverhealth.controller;

import com.silverhealth.common.PageResult;
import com.silverhealth.common.Result;
import com.silverhealth.entity.Community;
import com.silverhealth.entity.SysUser;
import com.silverhealth.entity.VisitRecord;
import com.silverhealth.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    @Autowired
    private RiskAlertService alertService;
    @Autowired
    private VisitRecordService visitRecordService;

    @GetMapping("/detail/{id}")
    public Result<Community> detail(@PathVariable Long id) {
        return Result.success(communityService.getCommunityDetail(id));
    }

    @GetMapping("/dashboard/{communityId}")
    public Result<Map<String, Object>> dashboard(@PathVariable Long communityId) {
        Map<String, Object> data = new HashMap<>();
        data.put("elderCount", userService.countEldersByCommunity(communityId));
        data.put("aloneCount", userService.countAloneElders(communityId));
        data.put("highAgeCount", userService.countHighAgeElders(communityId, 80));
        data.put("todayAlertCount", alertService.countTodayAlerts(communityId));
        data.put("unhandledAlerts", alertService.getUnhandledAlerts(communityId));
        return Result.success(data);
    }

    @GetMapping("/elders/{communityId}")
    public Result<PageResult<SysUser>> elderList(
            @PathVariable Long communityId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(userService.getElderList(communityId, keyword, page, size));
    }

    @PostMapping("/visit-record")
    public Result<VisitRecord> addVisitRecord(@RequestBody VisitRecord record) {
        return Result.success(visitRecordService.addRecord(record));
    }

    @GetMapping("/visit-records/elder/{elderId}")
    public Result<PageResult<VisitRecord>> visitRecordsByElder(
            @PathVariable Long elderId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(visitRecordService.getRecordsByElder(elderId, page, size));
    }
}

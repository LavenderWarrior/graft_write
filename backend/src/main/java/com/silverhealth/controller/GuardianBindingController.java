package com.silverhealth.controller;

import com.silverhealth.common.Result;
import com.silverhealth.entity.GuardianBinding;
import com.silverhealth.service.GuardianBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/binding")
public class GuardianBindingController {

    @Autowired
    private GuardianBindingService bindingService;

    @PostMapping("/generate-code")
    public Result<String> generateCode(@RequestParam Long elderId) {
        return Result.success(bindingService.generateBindCode(elderId));
    }

    @PostMapping("/bind-by-code")
    public Result<GuardianBinding> bindByCode(@RequestParam String code,
                                               @RequestParam Long guardianId,
                                               @RequestParam Integer guardianType) {
        GuardianBinding binding = bindingService.bindByCode(code, guardianId, guardianType);
        if (binding == null) {
            return Result.error("绑定码无效或已使用");
        }
        return Result.success(binding);
    }

    @PostMapping("/bind-direct")
    public Result<GuardianBinding> bindDirect(@RequestParam Long elderId,
                                               @RequestParam Long guardianId,
                                               @RequestParam Integer guardianType) {
        return Result.success(bindingService.bindByWechatId(elderId, guardianId, guardianType));
    }

    @PostMapping("/unbind/{bindingId}")
    public Result<Void> unbind(@PathVariable Long bindingId) {
        bindingService.unbind(bindingId);
        return Result.success();
    }

    @GetMapping("/elder/{elderId}")
    public Result<List<GuardianBinding>> elderBindings(@PathVariable Long elderId) {
        return Result.success(bindingService.getBindingsByElder(elderId));
    }

    @GetMapping("/guardian/{guardianId}")
    public Result<List<GuardianBinding>> guardianBindings(@PathVariable Long guardianId) {
        return Result.success(bindingService.getBindingsByGuardian(guardianId));
    }
}

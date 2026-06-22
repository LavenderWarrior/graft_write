package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.GuardianBinding;
import com.silverhealth.entity.SysUser;
import com.silverhealth.mapper.GuardianBindingMapper;
import com.silverhealth.mapper.SysUserMapper;
import com.silverhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Autowired
    private GuardianBindingMapper guardianBindingMapper;

    @Override
    public SysUser loginByOpenid(String openid, Integer userType) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getOpenid, openid).eq(SysUser::getUserType, userType);
        return getOne(wrapper);
    }

    @Override
    public SysUser register(SysUser user) {
        save(user);
        return user;
    }

    @Override
    public SysUser getUserDetail(Long userId) {
        return getById(userId);
    }

    @Override
    public PageResult<SysUser> getElderList(Long communityId, String keyword, Integer page, Integer size) {
        Page<SysUser> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, 1);
        if (communityId != null) {
            wrapper.eq(SysUser::getCommunityId, communityId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(SysUser::getRealName, keyword)
                    .or().like(SysUser::getPhone, keyword));
        }
        wrapper.orderByDesc(SysUser::getCreatedAt);
        Page<SysUser> result = page(pageParam, wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }

    @Override
    public List<SysUser> getBoundElders(Long guardianId) {
        LambdaQueryWrapper<GuardianBinding> bw = new LambdaQueryWrapper<>();
        bw.eq(GuardianBinding::getGuardianId, guardianId).eq(GuardianBinding::getStatus, 1);
        List<GuardianBinding> bindings = guardianBindingMapper.selectList(bw);
        if (bindings.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> elderIds = bindings.stream().map(GuardianBinding::getElderId).collect(Collectors.toList());
        return listByIds(elderIds);
    }

    @Override
    public long countEldersByCommunity(Long communityId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, 1).eq(SysUser::getCommunityId, communityId);
        return count(wrapper);
    }

    @Override
    public long countAloneElders(Long communityId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, 1)
                .eq(SysUser::getCommunityId, communityId)
                .eq(SysUser::getIsAlone, 1);
        return count(wrapper);
    }

    @Override
    public long countHighAgeElders(Long communityId, int ageThreshold) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, 1)
                .eq(SysUser::getCommunityId, communityId)
                .ge(SysUser::getAge, ageThreshold);
        return count(wrapper);
    }
}

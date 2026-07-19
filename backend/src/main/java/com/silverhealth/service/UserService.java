package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.SysUser;

import java.util.List;

public interface UserService extends IService<SysUser> {

    SysUser loginByOpenid(String openid, Integer userType);

    SysUser register(SysUser user);

    SysUser getUserDetail(Long userId);

    PageResult<SysUser> getElderList(Long communityId, String keyword, Integer page, Integer size);

    List<SysUser> getBoundElders(Long guardianId);

    long countEldersByCommunity(Long communityId);

    long countAloneElders(Long communityId);

    long countHighAgeElders(Long communityId, int ageThreshold);
}

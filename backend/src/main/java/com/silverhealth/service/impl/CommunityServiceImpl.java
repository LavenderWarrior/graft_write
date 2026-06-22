package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.entity.Community;
import com.silverhealth.mapper.CommunityMapper;
import com.silverhealth.service.CommunityService;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Override
    public Community getCommunityDetail(Long communityId) {
        return getById(communityId);
    }
}

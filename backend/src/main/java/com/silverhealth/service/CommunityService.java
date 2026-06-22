package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.entity.Community;

public interface CommunityService extends IService<Community> {

    Community getCommunityDetail(Long communityId);
}

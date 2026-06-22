package com.silverhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silverhealth.entity.Membership;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MembershipMapper extends BaseMapper<Membership> {
}

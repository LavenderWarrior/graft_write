package com.silverhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.VisitRecord;
import com.silverhealth.mapper.VisitRecordMapper;
import com.silverhealth.service.VisitRecordService;
import org.springframework.stereotype.Service;

@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord>
        implements VisitRecordService {

    @Override
    public VisitRecord addRecord(VisitRecord record) {
        save(record);
        return record;
    }

    @Override
    public PageResult<VisitRecord> getRecordsByElder(Long elderId, Integer page, Integer size) {
        Page<VisitRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<VisitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VisitRecord::getElderId, elderId).orderByDesc(VisitRecord::getVisitTime);
        Page<VisitRecord> result = page(pageParam, wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }

    @Override
    public PageResult<VisitRecord> getRecordsByWorker(Long workerId, Integer page, Integer size) {
        Page<VisitRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<VisitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VisitRecord::getWorkerId, workerId).orderByDesc(VisitRecord::getVisitTime);
        Page<VisitRecord> result = page(pageParam, wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }
}

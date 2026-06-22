package com.silverhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silverhealth.common.PageResult;
import com.silverhealth.entity.VisitRecord;

public interface VisitRecordService extends IService<VisitRecord> {

    VisitRecord addRecord(VisitRecord record);

    PageResult<VisitRecord> getRecordsByElder(Long elderId, Integer page, Integer size);

    PageResult<VisitRecord> getRecordsByWorker(Long workerId, Integer page, Integer size);
}

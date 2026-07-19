package com.silverhealth.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private long page;
    private long size;
    private List<T> records;

    public static <T> PageResult<T> of(long total, long page, long size, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.setTotal(total);
        r.setPage(page);
        r.setSize(size);
        r.setRecords(records);
        return r;
    }
}

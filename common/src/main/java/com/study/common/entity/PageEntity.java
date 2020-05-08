package com.study.common.entity;

import java.util.List;

/**
 * @author chenglutao
 */
public class PageEntity<T> extends RespEntity<List<T>> {
    private long total;
    private int pageNum;
    private int pageSize;

    public PageEntity() {
    }

    public PageEntity(List<T> data, long total) {
        super(Key.OK);
        this.data = data;
        this.total = total;
    }


    public PageEntity(List<T> data, long total,long sum) {
        super(Key.OK);
        this.data = data;
        this.total = total;
        this.sum = sum ;
    }


    public long getTotal() {
        return total;
    }

    public PageEntity setTotal(long total) {
        this.total = total;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public PageEntity setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageEntity setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}

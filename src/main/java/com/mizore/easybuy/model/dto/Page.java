package com.mizore.easybuy.model.dto;

import lombok.Data;

@Data
public class Page {

    // 一页多少条记录
    private int pageSize;

    // 页码
    private int pageNum;

    // 总共多少页
    private int pages;

    // 总共多少条记录
    private int total;
}

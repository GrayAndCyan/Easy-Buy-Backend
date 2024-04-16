package com.mizore.easybuy.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageVO {

    // 一页多少条记录
    private int pageSize;

    // 页码
    private int pageNum;

    // 总共多少页
    private int pages;

    // 总共多少条记录
    private long total;

    public PageVO(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}

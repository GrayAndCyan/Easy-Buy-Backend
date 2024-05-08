package com.mizore.easybuy.model.query;

import lombok.Data;

@Data
public class PageQuery {

    //页码
    private int pageNum;

    //每页显示记录数
    private int pageSize;

}

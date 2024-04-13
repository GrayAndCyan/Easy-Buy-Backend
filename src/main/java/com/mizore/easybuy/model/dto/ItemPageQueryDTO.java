package com.mizore.easybuy.model.dto;

import lombok.Data;

@Data
public class ItemPageQueryDTO {

    //商品标题
    private String title;

    //商品分类ID
    private int categoryId;

    //页码
    private int pageNum;

    //每页显示记录数
    private int pageSize;

}

package com.mizore.easybuy.model.query;

import lombok.Data;

@Data
public class SellerUpdateQuery {

    private Integer sellerId;  // 要修改的店铺id

    private String address;

}

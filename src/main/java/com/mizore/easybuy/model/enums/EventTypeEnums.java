package com.mizore.easybuy.model.enums;

public enum EventTypeEnums {

    UNKNOWN("Unknown"),

    // buyer action
    // 买家下单
    BUYER_PLACE_ORDER("Buyer-Place-Order"),

    // 买家创建支付单
    BUYER_CREATE_PAY("Buyer-Create-Pay"),
    // 买家支付订单
    BUYER_PAY_ORDER("Buyer-Pay-Order"),
    // 买家确定收货
    BUYER_CONFIRM_RECEIPT("Buyer-Confirm-Receipt"),
    // 买家评价
    BUYER_REVIEW("Buyer-Review"),
    // 买家取消订单
    BUYER_CANCEL_ORDER("Buyer-Cancel-Order"),
    // 买家投诉
    BUYER_COMPLAINT("Buyer-Complaint"),

    // seller action
    // 卖家发货
    SELLER_SEND_ORDER("Seller-Send-Order"),
    // 卖家投诉
    SELLER_COMPLAINT("Seller-Complaint")


    // admin action



    ;




    private final String desc;

    public final String getDesc() {
        return desc;
    }

    EventTypeEnums(String desc) {
        this.desc = desc;
    }
}

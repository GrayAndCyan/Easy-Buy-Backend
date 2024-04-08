package com.mizore.easybuy.model.enums;

public enum OrderStatusEnum {

    PENDING_PAYMENT(100,"待付款"),

    PROCESSING(200, "处理中"),

    SHIPPED(300, "已发货"),

    DELIVERED(400, "已收货"),

    COMMENTED(500, "已评价"),

    CANCELLED(600, "已取消");

    private int code;

    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

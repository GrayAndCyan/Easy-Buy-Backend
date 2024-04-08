package com.mizore.easybuy.model.enums;

public enum DeleteEnum {

    NOT_DELETED(0, "未删除"),

    DELETED(1, "已删除")
    ;

    private int code;

    private String desc;

    DeleteEnum(int code, String desc) {
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

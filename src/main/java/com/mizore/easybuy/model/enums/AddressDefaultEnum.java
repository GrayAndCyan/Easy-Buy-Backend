package com.mizore.easybuy.model.enums;

public enum AddressDefaultEnum {

    NOT_DEFAULT(0, "非默认"),

    DEFAULT(1, "默认");

    private int code;

    private String desc;

    AddressDefaultEnum(int code, String desc) {
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

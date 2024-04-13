package com.mizore.easybuy.model.enums;

public enum RoleEnum {

    BUYER(1, "普通买家"),

    SELLER(2, "卖家"),

    ADMIN(3, "管理员");

    private Integer code;

    private String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

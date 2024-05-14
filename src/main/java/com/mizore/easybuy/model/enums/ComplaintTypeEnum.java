package com.mizore.easybuy.model.enums;

import java.util.Objects;

public enum ComplaintTypeEnum {

    BUYER_COMPLAINT(1, "买家发起投诉"),

    SELLER_COMPLAINT(2, "商家发起投诉");

    private Integer code;

    private String desc;

    public static ComplaintTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ComplaintTypeEnum type : values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    ComplaintTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

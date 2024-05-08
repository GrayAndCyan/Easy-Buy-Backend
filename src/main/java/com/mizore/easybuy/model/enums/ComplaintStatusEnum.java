package com.mizore.easybuy.model.enums;

import java.util.Objects;

public enum ComplaintStatusEnum {

    PROCESSING(1, "处理中"),

    FINISHED(2, "已完成");

    private Integer code;

    private String desc;

    ComplaintStatusEnum(Integer code, String desc) {
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

    public static ComplaintStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ComplaintStatusEnum status : values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }
}

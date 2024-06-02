package com.mizore.easybuy.model.enums;

// 客体类型，用于审计记录action作用的对象 类型
public enum ObjectTypeEnums {
    ORDER("order"),

    COMPLAINT("complaint")
    ;

    public String getDesc() {
        return desc;
    }

    ObjectTypeEnums(String desc) {
        this.desc = desc;
    }

    private String desc;
}

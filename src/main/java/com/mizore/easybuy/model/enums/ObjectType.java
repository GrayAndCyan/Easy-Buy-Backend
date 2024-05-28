package com.mizore.easybuy.model.enums;

// 客体类型，用于审计记录action作用的对象 类型
public enum ObjectType {
    ORDER("order");

    public String getDesc() {
        return desc;
    }

    ObjectType(String desc) {
        this.desc = desc;
    }

    private String desc;
}

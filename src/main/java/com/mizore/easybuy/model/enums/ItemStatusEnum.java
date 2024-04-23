package com.mizore.easybuy.model.enums;

public enum ItemStatusEnum {

    ON_SALE(1,"On sale"),

    OUT_SALE(2, "Off sale");

    private Integer code;

    private String desc;

    ItemStatusEnum(int code, String desc) {
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

    public static String getDescByCode(Integer code) {
        ItemStatusEnum[] values = ItemStatusEnum.values();
        for (ItemStatusEnum status : values) {
            if (status.code.equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }

}

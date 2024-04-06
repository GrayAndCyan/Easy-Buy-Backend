package com.mizore.easybuy.model.enums;

public enum ReturnEnum {
    SUCCESS("成功", 200),
    FAILURE("失败", -500);

    private final String message;

    private final int code;

    ReturnEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

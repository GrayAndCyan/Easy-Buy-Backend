package com.mizore.easybuy.model.vo;

import com.mizore.easybuy.model.enums.ReturnEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BaseVO<T> {

    private int code;

    private String message;

    private T data;

    public BaseVO<T> success() {
        this.code = ReturnEnum.SUCCESS.getCode();
        this.message = ReturnEnum.SUCCESS.getMessage();
        return this;
    }

    public BaseVO<T> success(T object) {
        this.code = ReturnEnum.SUCCESS.getCode();
        this.message = ReturnEnum.SUCCESS.getMessage();
        this.data = object;
        return this;
    }

    public BaseVO<T> failure() {
        this.code = ReturnEnum.FAILURE.getCode();
        this.message = ReturnEnum.FAILURE.getMessage();
        return this;
    }

    public int getCode() {
        return code;
    }

    public BaseVO<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseVO<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseVO<T> setData(T data) {
        this.data = data;
        return this;
    }
}

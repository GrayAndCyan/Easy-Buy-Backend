package com.mizore.easybuy.model.vo;

import com.mizore.easybuy.model.enums.ReturnEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
}

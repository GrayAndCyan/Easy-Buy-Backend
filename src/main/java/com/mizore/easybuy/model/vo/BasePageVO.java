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
public class BasePageVO <T>{

    private int code;

    private String message;

    private T data;

    public BasePageVO<T> success() {
        this.code = ReturnEnum.SUCCESS.getCode();
        this.message = ReturnEnum.SUCCESS.getMessage();
        return this;
    }

    /**
     * 分页相关信息
     */
    private PageVO page;
}

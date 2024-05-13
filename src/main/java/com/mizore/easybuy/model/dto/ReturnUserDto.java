package com.mizore.easybuy.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnUserDto {
    private int id;

    private String username;

    private String addr_phone;

    private int role;

}

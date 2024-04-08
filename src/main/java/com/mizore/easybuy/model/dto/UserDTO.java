package com.mizore.easybuy.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private int id;

    private String username;

    private byte role;
}

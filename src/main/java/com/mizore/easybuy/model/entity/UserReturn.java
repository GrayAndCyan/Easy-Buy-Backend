package com.mizore.easybuy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReturn {
    
    private int id;

    private String username;

    private String addr_phone;

    private int role;
    
    private int status;

}

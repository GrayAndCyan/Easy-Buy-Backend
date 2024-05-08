package com.mizore.easybuy.model.query;

import lombok.Data;

@Data
public class BanQuery {

    private int userId;

    private String reason;

    private long duration;
}

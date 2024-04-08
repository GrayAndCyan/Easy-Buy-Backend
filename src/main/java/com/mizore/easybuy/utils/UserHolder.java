package com.mizore.easybuy.utils;

import com.mizore.easybuy.model.dto.UserDTO;

// 使用 threadLocal 保持用户对象上下文
public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void save(UserDTO user) {
        tl.set(user);
    }

    public static UserDTO get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

}

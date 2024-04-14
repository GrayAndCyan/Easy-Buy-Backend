package com.mizore.easybuy;

import com.google.common.collect.Maps;
import com.mizore.easybuy.utils.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class JWTTest {

    @Test
    public void testSec() {
//        String secretKeyStr = JWTUtil.getSecretKeyStr();
        // huHAQ21P5fsGgpnHKipREwz4zgwUxJ8bGl+gmW4mbl4=
        System.out.println();
    }

    @Test
    public void getAToken() {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("id", 1);
        claims.put("username", "mizore");
        claims.put("role", 1);
        String jwt = JWTUtil.generateJWT(claims);
        System.out.println(jwt);
    }

}

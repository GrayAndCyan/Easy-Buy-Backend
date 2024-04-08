package com.mizore.easybuy;

import com.mizore.easybuy.utils.JWT;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

@SpringBootTest
public class JWTTest {

    @Test
    public void testSec() {
        String secretKeyStr = JWT.getSecretKeyStr();
        // huHAQ21P5fsGgpnHKipREwz4zgwUxJ8bGl+gmW4mbl4=
        System.out.println();
    }

}

package com.decade.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class UserCenterApplicationTests {
    @Test
    void digest(){
        String result = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(result);
    }
    @Test
    void contextLoads() {
    }

}

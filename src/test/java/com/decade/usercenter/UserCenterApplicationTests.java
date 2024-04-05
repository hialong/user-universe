package com.decade.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;

@SpringBootTest
class UserCenterApplicationTests {
    private final Object Lock = new Object();
    @Test
    void digest(){
        String result = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(result);
    }
    @Test
    void contextLoads() throws InterruptedException {
        synchronized (Lock){
            for(int i = 0;i<10;i++){
                System.out.println(i +":"+System.currentTimeMillis());
                Thread.sleep(10000);
            }
            Lock.notifyAll();
        }

    }

}

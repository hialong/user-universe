package com.decade.usercenter.service.impl;



import com.decade.hapicommon.service.UserInterfaceInfoCommonService;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class UserInterfaceInfoServiceImplTest {

    @Resource
    private UserInterfaceInfoCommonService userInterfaceInfoCommonService;

    @Test
    public void invokeCount() {
        boolean b = userInterfaceInfoCommonService.invokeCount(1L, 4L);
        assertTrue(b);
    }
}
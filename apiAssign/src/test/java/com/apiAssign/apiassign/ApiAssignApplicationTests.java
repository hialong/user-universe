package com.apiAssign.apiassign;

import com.decade.apiassignclientsdk.client.HapiClient;
import com.decade.apiassignclientsdk.model.Someting;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ApiAssignApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private HapiClient hapiClient;

    //private HapiClient hapiClient = new HapiClient("hailong", "abcdefg");

    @Test
     void contextLoads() {
        List<String> beanNames = Arrays.asList(applicationContext.getBeanDefinitionNames());
        beanNames.forEach(System.out::println);
    }

    @Test
    void test() {
        hapiClient.getRandomNumByGet("吃夜宵");
        hapiClient.eatByPost("炒面");
        hapiClient.doSomething(new Someting("吃夜宵","hailong"));
    }



}

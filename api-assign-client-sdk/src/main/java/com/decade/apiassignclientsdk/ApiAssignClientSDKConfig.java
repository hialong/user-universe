package com.decade.apiassignclientsdk;

import com.decade.apiassignclientsdk.client.HapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
// 通过配置文件读取配置 accessKey, secretKey
@ConfigurationProperties("hapi.client")
@Data
@ComponentScan
public class ApiAssignClientSDKConfig {
    private String accessKey;
    private String secretKey;

    /**
     * 利用配置里面的配置信息，创建hapiClient
     * @return 客户端对象
     */
    @Bean
    public HapiClient hapiClient() {
        return new HapiClient(secretKey, accessKey);
    }

}

package com.decade.usercenter.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Knife4j配置
 * 文档地址 http://127.0.0.1:8881/api/doc.html
 *
 * @author hailong
 */
@Configuration
@Profile({"dev", "test"})
@EnableKnife4j
public class knife4jConfig {
    @Bean
    public OpenAPI defaultApi2() {
        return new OpenAPI().info(new Info()
                        .title("接口文档")
                        .description("管理中心文档")
                        .contact(new Contact().name("hailong"))
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation().description("外部文档").url("https://springshop.wiki.github.org/docs"));
    }
}

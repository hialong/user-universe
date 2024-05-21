package com.decade.apigateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
public class ApiGatewayApplication {


    public static void main(String[] args){
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    //@Bean
    //public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //    return builder.routes()
    //            .route("tobaidu", r -> r.path("/baidu")
    //                    .uri("https://www.baidu.com"))
    //            .route("host_route", r -> r.host("*.myhost.org")
    //                    .uri("http://httpbin.org"))
    //            .route("rewrite_route", r -> r.host("*.rewrite.org")
    //                    .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
    //                    .uri("http://httpbin.org"))
    //            .build();
    //}

}

package com.decade.apiassignclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.decade.apiassignclientsdk.model.Someting;
import com.decade.apiassignclientsdk.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;


import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * api分发平台客户端，作用是提供调用接口能力，并且做安全校验
 * 简单做演示提供几个api
 * @author hailong
 */

@Slf4j
public class HapiClient {

    private static final String GATEWAY_HOST = "http://8.136.96.241:9991";
    private String secretKey;

    private String accessKey;

    public HapiClient(String secretKey, String accessKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    /**
     * 获取请求头
     * @param body
     * @return
     */
    private HashMap<String, String> getHeaderMap(String body) {
        HashMap<String, String> header = new HashMap<>();
        //后面加入随机数校验
        header.put("nonce", RandomUtil.randomNumbers(4));
        header.put("accessKey", accessKey);
        String encode = "";
        try{
            encode = URLEncoder.encode(body, CharsetUtil.UTF_8);
        }catch (Exception e){
            throw new RuntimeException("编码失败");
        }
        header.put("body", encode);
        header.put("sign", SignUtil.genSign(encode, secretKey));
        header.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        return header;
    }

    public String getRandomNumByGet(String des) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST+"/api/random/?des="+des)
                .body(des)
                .headerMap(getHeaderMap(des), false).execute();
        int status = httpResponse.getStatus();
        System.out.println(status);
        String body = httpResponse.body();
        System.out.println(body);
        return body;

    }

    public String eatByPost(String food) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/random/food?food=" +food)
                .headerMap(getHeaderMap(food), false).execute();
        int status = httpResponse.getStatus();
        System.out.println(status);
        String body = httpResponse.body();
        System.out.println(body);
        return body;
    }

    public String doSomething(Someting thing) {
        log.info("地址：{}",GATEWAY_HOST+"/api/random/PostDo");
        String jsonStr = JSONUtil.toJsonStr(thing);
        log.info("参数：{}",thing);
        HashMap<String, String> headerMap = getHeaderMap(jsonStr);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/random/PostDo")
                .headerMap(headerMap, false)
                .body(jsonStr)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        System.out.println(body);
        return body;
    }

}

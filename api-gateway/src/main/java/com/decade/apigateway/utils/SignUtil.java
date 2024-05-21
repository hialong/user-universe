package com.decade.apigateway.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.decade.hapicommon.service.UserCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 签名工具类
 *
 * @author hailong
 */
@Slf4j
public class SignUtil {

    public static String genSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return digester.digestHex(content);
    }

    public static Boolean CheckSign(ServerHttpRequest request, String secretKey) {
        log.info("secretKey:{}",secretKey);
        String accessKey = request.getHeaders().getFirst("accessKey");
        String nonce = request.getHeaders().getFirst("nonce");
        String timestamp = request.getHeaders().getFirst("timestamp");
        String sign = request.getHeaders().getFirst("sign");
        String body = request.getHeaders().getFirst("body");
        if (StringUtils.isAnyBlank(accessKey, nonce, timestamp, sign, secretKey)) {
            log.error("请求参数为空accessKey, nonce, timestamp, sign, secretKey");
            throw new RuntimeException("没有权限");
        }

        //实际情况是用redis缓存一部分随机数，使用后废弃
        if (nonce != null && Long.parseLong(nonce) > 10000) {
            log.error("nonce");
            throw new RuntimeException("没有权限");
        }
        // 校验时间戳和当前时间不要差十分钟
        int TEN_MINUTE = 60 * 10;
        if (timestamp != null && Math.abs(Long.parseLong(timestamp) - System.currentTimeMillis() / 1000) > TEN_MINUTE) {
            log.error("timestamp");
            throw new RuntimeException("没有权限");

        }
        String gennedSign = genSign(body, secretKey);
        if (gennedSign == null || !gennedSign.equals(sign)) {
            log.error("sign:{},genneSign:{}", sign, gennedSign);
            throw new RuntimeException("没有权限");
        }
        return true;
    }
}

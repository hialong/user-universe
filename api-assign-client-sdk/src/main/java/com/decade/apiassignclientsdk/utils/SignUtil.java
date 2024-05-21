package com.decade.apiassignclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 签名工具类
 *
 * @author hailong
 */
public class SignUtil {
    public static String genSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return digester.digestHex(content);
    }

    //public static Boolean CheckSign(HttpServletRequest request) {
    //    System.out.println("request是" + request);
    //    String accessKey = request.getHeader("accessKey");
    //    String nonce = request.getHeader("nonce");
    //    String timestamp = request.getHeader("timestamp");
    //    String sign = request.getHeader("sign");
    //    String body = request.getHeader("body");
    //    //实际情况是用redis缓存一部分随机数，使用后废弃
    //    if (Long.parseLong(nonce) > 10000) {
    //        throw new RuntimeException("没有权限");
    //    }
    //    //实际情况是用accessKey去查数据库找到对应的secretKey
    //    String secretKey = "abcdefg";
    //    // 校验时间戳和当前时间不要差十分钟
    //    if (Math.abs(Long.parseLong(timestamp) - System.currentTimeMillis() / 1000) > 60 * 10) {
    //        throw new RuntimeException("没有权限");
    //    }
    //    String gennedSign = genSign(body, secretKey);
    //    if (!gennedSign.equals(sign)) {
    //        throw new RuntimeException("没有权限");
    //    }
    //    return true;
    //}
}

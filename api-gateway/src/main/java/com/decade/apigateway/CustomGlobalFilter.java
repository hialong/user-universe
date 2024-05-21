package com.decade.apigateway;

import com.decade.apigateway.utils.SignUtil;
import com.decade.hapicommon.model.domain.InterfaceInfo;
import com.decade.hapicommon.model.domain.User;
import com.decade.hapicommon.model.domain.UserInterfaceInfo;
import com.decade.hapicommon.service.InterfaceInfoCommonService;
import com.decade.hapicommon.service.UserCommonService;
import com.decade.hapicommon.service.UserInterfaceInfoCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *  全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final ArrayList<String> BLACKLIST = new ArrayList<>();

    @DubboReference
    private UserInterfaceInfoCommonService userInterfaceInfoCommonService;

    @DubboReference
    private InterfaceInfoCommonService interfaceInfoCommonService;

    @DubboReference
    private UserCommonService userCommonService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //需要实现的功能
        //1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：{}", request.getId());
        String path = request.getPath().toString();
        //4请求的接口是否存在,从数据库里面取接口信息，看是否存在，是否打开
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 配置文件中配置的route的uri属性(匹配到的route)
        assert route != null;
        String uri = route.getUri().toString();
        String url = uri + path;
        log.info("请求路径：{}", url);
        log.info("请求参数：{}",request.getQueryParams());
        log.info("请求头：{}",request.getHeaders());
        String method = request.getMethod().toString();
        log.info("请求方法：{}", method);
        String remoteAddress = request.getRemoteAddress().getHostString();
        log.info("请求来源地址：{}", remoteAddress);
        //2黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if(BLACKLIST.contains(remoteAddress)){
            return handleNoAuth(response);
        }
        //3鉴权
        String accessKey = request.getHeaders().getFirst("accessKey");
        log.info("accessKey:{}",accessKey);
        User invokeUser = null;
        try {
           invokeUser = userCommonService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("getInvokeUser",e);
            return handleNoAuth(response);
        }
        String seceretKey = invokeUser.getSeceretKey();
        if(!SignUtil.CheckSign(request,seceretKey)){
            log.error("权限检测不正确");
            return handleNoAuth(response);
        }
        InterfaceInfo interfaceInfo = interfaceInfoCommonService.getInterfaceInfo(url, method);
        if(interfaceInfo == null){
            log.error("接口不存在:{}",interfaceInfo);
            return handleNoAuth(response);
        }
        //在调用之前，要判断当前是否还有调用次数，不然日志里面那个始终记录不到
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoCommonService.getUserInterfaceInfo(interfaceInfo.getId(), invokeUser.getId());
        if(userInterfaceInfo == null || userInterfaceInfo.getLeftNum() <= 0){
            log.error("接口不存在");
            return handleNoAuth(response);
        }
        //Mono<Void> filter = chain.filter(exchange);
        //5请求转发，调用模拟接口 响应日志
        return handleResponse(exchange, chain, interfaceInfo.getId(),invokeUser.getId());





    }

    /**
     * 处理响应日志
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long InterfaceId,long userId){
        try {
            //从交换寄拿响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓冲区工厂，拿到缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatusCode statusCode = originalResponse.getStatusCode();
            if(statusCode == HttpStatus.OK){
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    //等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        //对象是响应式的
                        if (body instanceof Flux) {
                            //我们拿到真正的body
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //往返回值里面写数据
                            //拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //接口调用成功了数据库加一
                                try {
                                    userInterfaceInfoCommonService.invokeCount(InterfaceId,userId);
                                } catch (Exception e) {
                                    //todo 应该接一个告警器
                                    log.error("invokeCount error",e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }

    }
    private static Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    // todo 这些个抛异常的都最好掉用usercenter那边的显示更清晰的异常类型
    private static Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    // 优先级
    @Override
    public int getOrder() {
        return -1;
    }
}
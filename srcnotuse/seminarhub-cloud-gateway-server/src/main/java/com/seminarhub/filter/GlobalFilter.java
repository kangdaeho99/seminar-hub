package com.seminarhub.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

/**
 * [ 2023-08-18 daeho.kang ]
 * Description : Cloud GateWay Global Filter
 *
 * Non Blocking, Asynchronous 의 Netty Server를 사용합니다.
 * ( SpringMVC가 아니기에 MONO/WebFlux 로 Return 합니다. )
 */
@Component
@Log4j2
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    // 처리시간 측정을 위해 StopWathch 객체 선언
    private StopWatch stopWatch;

    public GlobalFilter(){
        super(Config.class);
        stopWatch = new StopWatch("API Gateway");
    }

    //Config inner class
    public static class Config{}

    //필터 메인
    /**
     * [ 2023-08-17 daeho.kang ]
     * Description :
     * Log를 찍어주는 Filter입니다.
     */
    @Override
    public GatewayFilter apply(Config config){
        return (((exchange, chain) -> {
            // Request, Response 객체 가져오기
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // PRE
            stopWatch.start();
            log.info("[Global Filter] Request >>>> IP : {}, URI :{}", request.getRemoteAddress().getAddress(), request.getURI());

            //POST
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                stopWatch.stop();
                log.info("[Global Filter] Response >>>> IP : {}, URI : {}, Resp Code:{} --> TakeTime :{} ms",
                        request.getRemoteAddress().getAddress(),
                        request.getURI(),
                        response.getStatusCode(),
                        stopWatch.getLastTaskTimeMillis()
                    );
            }));
        }));
    }

}

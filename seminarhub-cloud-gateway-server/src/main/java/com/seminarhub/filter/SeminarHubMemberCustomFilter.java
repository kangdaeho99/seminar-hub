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
 * Description : SeminarHubMemberCustomFilter
 *
 * Non Blocking, Asynchronous 의 Netty Server를 사용합니다.
 * ( SpringMVC가 아니기에 MONO/WebFlux 로 Return 합니다. )
 */
@Component
@Log4j2
public class SeminarHubMemberCustomFilter extends AbstractGatewayFilterFactory<SeminarHubMemberCustomFilter.Config> {

    // 처리시간 측정을 위해 StopWathc 객체 선언
    private StopWatch stopWatch;

    public SeminarHubMemberCustomFilter(){
        super(Config.class);
        stopWatch = new StopWatch("API Gateway");
    }

    //Config inner class
    public static class Config{}

    //필터 메인
    /**
     * [ 2023-08-17 daeho.kang ]
     * Description :
     * 서버의 Request, Response, Time Log를 찍습니다.
     */
    @Override
    public GatewayFilter apply(Config config){
        return (((exchange, chain) -> {
            // Request, Response 객체 가져오기
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // PRE
            stopWatch.start();
            log.info("[SeminarHubMember Custom Filter] Request >>>> IP : {}, URI :{}", request.getRemoteAddress().getAddress(), request.getURI());
            request.getHeaders().forEach( (key, value) ->{
                log.info("      [request Headers] {} : {}", key, value); //Request Headers Log
            });

            //POST
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                stopWatch.stop();
                log.info("      ---------------------------------------");
                response.getHeaders().forEach( (key, value) ->{
                    log.info("      [Response Header] {} : {}", key, value);
                });
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

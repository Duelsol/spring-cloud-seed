package me.duelsol.springcloudseed.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 冯奕骅
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断当前请求路径是否匹配
        RequestMappingInfo mapping = RequestMappingInfo.paths("/user/*").build().getMatchingCondition(exchange);
        if (mapping != null) {
            System.out.println(mapping);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}

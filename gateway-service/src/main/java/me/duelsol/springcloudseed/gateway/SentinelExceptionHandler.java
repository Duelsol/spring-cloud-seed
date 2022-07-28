package me.duelsol.springcloudseed.gateway;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author 冯奕骅
 */
public class SentinelExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable t) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(t);
        }
        String message;
        if (BlockException.isBlockException(t)) {
            if (t instanceof FlowException || t instanceof ParamFlowException) {
                message = "被限流了";
            } else if (t instanceof DegradeException) {
                message = "服务降级了";
            } else if (t instanceof SystemBlockException) {
                message = "被系统保护了";
            } else if (t instanceof AuthorityException) {
                message = "被授权了";
            } else {
                message = "未知";
            }
            response.getHeaders().add("Content-Type", "application/json");
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }
        return Mono.error(t);
    }

}

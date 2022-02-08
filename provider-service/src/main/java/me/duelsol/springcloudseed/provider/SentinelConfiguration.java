package me.duelsol.springcloudseed.provider;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 冯奕骅
 */
@Configuration
public class SentinelConfiguration implements BlockExceptionHandler {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        String message;
        if (e instanceof FlowException) {
            message = "被限流了";
        } else if (e instanceof DegradeException) {
            message = "服务降级了";
        } else if (e instanceof ParamFlowException) {
            message = "被限流了";
        } else if (e instanceof SystemBlockException) {
            message = "被系统保护了";
        } else if (e instanceof AuthorityException) {
            message = "被授权了";
        } else {
            message = "未知";
        }
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json");
        response.getWriter().print("{\"message\":\"" + message + "\"}");
    }

}

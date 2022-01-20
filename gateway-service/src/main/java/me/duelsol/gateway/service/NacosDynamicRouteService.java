package me.duelsol.gateway.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author 冯奕骅
 */
@Service
@Slf4j
public class NacosDynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 路由id
     */
    private static final List<String> ROUTE_IDS = Lists.newArrayList();

    /**
     * 监听Nacos路由配置，动态改变路由
     *
     * @param configInfo
     */
    @NacosConfigListener(dataId = "gateway-service")
    public void routeConfigListener(String configInfo) {
        clearRoute();
        try {
            List<RouteDefinition> gatewayRouteDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);
            for (RouteDefinition routeDefinition : gatewayRouteDefinitions) {
                addRoute(routeDefinition);
            }
            publish();
            log.info("Dynamic Routing Publish Success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 清空路由
     */
    private void clearRoute() {
        for (String id : ROUTE_IDS) {
            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        ROUTE_IDS.clear();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 添加路由
     *
     * @param definition
     */
    private void addRoute(RouteDefinition definition) {
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            ROUTE_IDS.add(definition.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发布路由、使路由生效
     */
    private void publish() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }

}

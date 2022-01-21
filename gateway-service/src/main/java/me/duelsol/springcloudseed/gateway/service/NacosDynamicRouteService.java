package me.duelsol.springcloudseed.gateway.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author 冯奕骅
 */
@Service
@Slf4j
public class NacosDynamicRouteService implements ApplicationEventPublisherAware {

    private static final String DATAID = "gateway";

    private static final String GROUP = "DEFAULT_GROUP";

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    private static final List<String> ROUTE_LIST = new ArrayList<>();

    @PostConstruct
    public void dynamicRouteConfigListener() {
        try {
            ConfigService configService = NacosFactory.createConfigService(serverAddr);
            String configInfo = configService.getConfig(DATAID, GROUP, 5000);
            parseRoutes(configInfo);
            configService.addListener(DATAID, GROUP, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    parseRoutes(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void parseRoutes(String configInfo) {
        clearRoutes();
        List<RouteDefinition> gatewayRouteDefinitions = new Gson().fromJson(configInfo, new TypeToken<List<RouteDefinition>>(){}.getType());
        for (RouteDefinition routeDefinition : gatewayRouteDefinitions) {
            addRoute(routeDefinition);
        }
        publish();
    }

    private void clearRoutes() {
        for (String id : ROUTE_LIST) {
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        ROUTE_LIST.clear();
    }

    private void addRoute(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        ROUTE_LIST.add(definition.getId());
    }

    private void publish() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}

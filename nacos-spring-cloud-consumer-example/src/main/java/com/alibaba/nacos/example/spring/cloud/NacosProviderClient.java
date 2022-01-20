package com.alibaba.nacos.example.spring.cloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 冯奕骅
 */
@FeignClient(name = "service-provider", fallback = NacosProviderFallback.class)
@Service
public interface NacosProviderClient {

    @GetMapping(value = "/echo/{string}")
    String echo(@PathVariable(value = "string") String string);

}

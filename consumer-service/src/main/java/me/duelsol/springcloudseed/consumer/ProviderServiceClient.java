package me.duelsol.springcloudseed.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 冯奕骅
 */
@FeignClient(name = "provider-service", fallbackFactory = ProviderServiceFallback.class)
public interface ProviderServiceClient {

    @GetMapping(value = "/echo/{string}")
    String echo(@PathVariable(value = "string") String string);

}

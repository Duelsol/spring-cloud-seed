package me.duelsol.springcloudseed.consumer;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author 冯奕骅
 */
@Component
public class ProviderServiceFallback implements FallbackFactory<ProviderServiceClient> {

    @Override
    public ProviderServiceClient create(Throwable cause) {
        return new ProviderServiceClient() {
            @Override
            public String echo(String string) {
                return "fallback";
            }
        };
    }

}

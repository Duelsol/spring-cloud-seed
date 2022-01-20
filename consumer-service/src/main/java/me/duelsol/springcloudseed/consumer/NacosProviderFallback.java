package me.duelsol.springcloudseed.consumer;

import org.springframework.stereotype.Service;

/**
 * @author 冯奕骅
 */
@Service
public class NacosProviderFallback implements NacosProviderClient {

    @Override
    public String echo(String string) {
        return "fallback";
    }

}

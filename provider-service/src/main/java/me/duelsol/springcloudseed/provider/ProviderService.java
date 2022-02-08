package me.duelsol.springcloudseed.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 冯奕骅
 */
@Service
public class ProviderService {

    @SentinelResource("echo")
    public String echo(@PathVariable String string) {
        return "Hello Nacos Discovery " + string;
    }

}

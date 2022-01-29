package me.duelsol.springcloudseed.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 冯奕骅
 */
@RestController
public class ConsumerServiceController {

    @Autowired
    private ProviderServiceClient providerServiceClient;

    @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
    public String echo(@PathVariable String str) {
        return providerServiceClient.echo(str);
    }

}

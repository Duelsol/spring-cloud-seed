package me.duelsol.springcloudseed.gateway;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @author 冯奕骅
 */
@Configuration
public class WebClientConfiguration {

    @LoadBalanced
    @Bean
    @Qualifier("lbWebClientBuilder")
    public WebClient.Builder lbWebClientBuilder() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient()));
    }

    @Bean
    @Qualifier("webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient()));
    }

    private HttpClient httpClient() {
        return HttpClient.create().tcpConfiguration(tcpClient ->
                tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                        .doOnConnected(connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(20))
                                        .addHandler(new WriteTimeoutHandler(20))
                        ));
    }

}

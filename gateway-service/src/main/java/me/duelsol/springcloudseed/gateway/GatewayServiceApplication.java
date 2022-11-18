package me.duelsol.springcloudseed.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
        System.setProperty("csp.sentinel.app.type", "1");
        System.setProperty("reactor.netty.pool.leasingStrategy", "lifo");
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}

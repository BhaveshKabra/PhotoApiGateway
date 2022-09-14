package org.bhavesh.photoapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfiguration {

	final Logger logger=LoggerFactory.getLogger(GlobalFiltersConfiguration.class); 
	
	@Bean
	@Order(3)
	public GlobalFilter secondPreFilter() {
		return (exchange,chain) -> {
			logger.info("Second Global Pre Filter Executed");
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("Second Global Post Filter Executed");	
			}));
		};
	}
	
	@Bean
	@Order(0)
	public GlobalFilter thirdPreFilter() {
		return (exchange,chain) -> {
			logger.info("Thhird Global Pre Filter Executed");
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("Third Global Post Filter Executed");	
			}));
		};
	}
}

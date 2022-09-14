package org.bhavesh.photoapp.api.gateway;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyPreFilter implements GlobalFilter {
	
	private final Logger logger=LoggerFactory.getLogger(MyPreFilter.class);
	
	@Override
	@Order(2)
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String requestPath=exchange.getRequest().getPath().toString();
		HttpHeaders headers =exchange.getRequest().getHeaders();
		logger.info("Within Pre-Filter");
		Set <String> headerSet=headers.keySet();
		logger.info("RequestPath= "+requestPath);
		headerSet.forEach( 
				(e)-> {String headerValue=headers.getFirst(e);
				logger.info(e +" "+headerValue );
				}
				);
		return chain.filter(exchange);
	}

}

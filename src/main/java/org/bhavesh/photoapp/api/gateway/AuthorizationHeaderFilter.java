package org.bhavesh.photoapp.api.gateway;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	private final Environment env;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}

	public static class Config {
		//put Configuration properties
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
			}
			String authheader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authheader.replace("Bearer", "");
				if(!isJwtValid(jwt))
				{
					onError(exchange, "Invalid Authoization Header", HttpStatus.UNAUTHORIZED);
				}
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}
	
	private boolean isJwtValid(String jwt)
	{
		boolean returnValue=true;
		try {
		SecretKeySpec secretKey = new SecretKeySpec(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		JwtParser jwtParser=Jwts.parserBuilder().setSigningKey(secretKey).build();
		if(!jwtParser.isSigned(jwt))
		{
			return false;
		}
		return returnValue;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

}

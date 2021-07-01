package com.libexamania.library;

import com.libexamania.library.httpclient.ExHttpClient;
import com.libexamania.library.httpclient.ExHttpException;
import com.libexamania.library.model.TokenValidatorResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
//@Configuration
//class Router{
//	@Bean
//	RouterFunction<ServerResponse> response(){
//		return RouterFunctions.route()
//				.GET("/",accept(APPLICATION_JSON),this::get)
//				.build();
//	}
//
//	Mono<ServerResponse> get(ServerRequest request){
//		ExHttpClient client = new ExHttpClient();
//		String AUTH_BASE_URL="https://authenservice.herokuapp.com";
//		String TOKEN_VALIDATE_END_POINT="/api/v1/token/validate";
//		String AUTHORIZATION_HEADER ="Authorization";
//		Mono<TokenValidatorResponse> tokenValidatorResponseMono = client
//				.baseURL(AUTH_BASE_URL+TOKEN_VALIDATE_END_POINT)
//				.setHttpMethod(HttpMethod.POST)
//				.setError(new ExHttpException("Unauthorized exception", HttpStatus.FORBIDDEN))
//				.setHeader(AUTHORIZATION_HEADER,"vfddbgdbbdfbdbd")
//				.executeMono(TokenValidatorResponse.class);
//		return
//	}
//}

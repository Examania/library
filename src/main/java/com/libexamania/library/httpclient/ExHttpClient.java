package com.libexamania.library.httpclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExHttpClient {
    //Variables for ExHttpClient
    private String BASE_URL;
    private Object body;
    private Class<? extends Object> bodyClass;
    private HttpMethod httpMethod;
    private Map<String,String> headerHashMap= new HashMap<>();
    private ExHttpException error;

    //Http client variable for http client
    private final HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));


    /*
     * Webclient variable for http client
     **/
    //TODO("It will be done when it goes to production 'uncomment the comment and add the timeout for all the request'")
    public WebClient getWebClient(){
        return WebClient.builder()
                .baseUrl(BASE_URL==null?"https://authenservice.herokuapp.com/api/v1/auth/token/validate":BASE_URL)
//            .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    //For setting the baseURL for request
    public ExHttpClient baseURL(String baseUrl){
        this.BASE_URL = baseUrl;
        return this;
    }
    //For setting the body for the request
    public ExHttpClient setBody(Object ob,Class<? extends Object> cl){
        this.body = ob;
        this.bodyClass = cl;
        return this;
    }

    //setting up the method of HttpMethod
    public ExHttpClient setHttpMethod(HttpMethod method){
        this.httpMethod = method;
        return this;
    }
    //setting up header name and body for setting up header
    public ExHttpClient setHeader(String headerName,String headerBody){
        headerHashMap.put(headerName,headerBody);
        return this;
    }
    //setting up error fallback for the request
    public <T extends ExHttpException> ExHttpClient setError(T error){
        this.error = error;
        return this;
    }

    @NonNull
    private WebClient.ResponseSpec execute(){
        httpMethod = (httpMethod==null)? HttpMethod.GET:httpMethod;
        WebClient.RequestBodyUriSpec requestBodyUriSpec = getWebClient()
                .method(httpMethod);
        WebClient.RequestBodySpec requestBodySpec = requestBodyUriSpec;
        for(Map.Entry mapElement : headerHashMap.entrySet()){
            String key = (String) mapElement.getKey();
            String value = (String) mapElement.getValue();
            requestBodySpec = requestBodySpec.header(key,value);
        }
        WebClient.RequestHeadersSpec requestHeadersSpec=null;
        if(body!=null && bodyClass!=null) {
            requestHeadersSpec= requestBodySpec.body(body, bodyClass);
        }
        WebClient.ResponseSpec responseSpec = (requestHeadersSpec==null)?requestBodySpec.retrieve():requestHeadersSpec.retrieve();
        responseSpec.onStatus(HttpStatus::isError, clientResponse -> Mono.error(error));
        return responseSpec;
    }

    //executing the method and getting the data
    public <T> Mono<T> executeMono(Class<T> cl){
        return execute()
                .bodyToMono(cl);
    }

    public <T> Flux<T> executeFlux(Class<T> cl){
        return execute().bodyToFlux(cl);
    }
}

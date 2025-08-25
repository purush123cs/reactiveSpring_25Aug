package com.example.demo.configuration;

import com.example.demo.exception.ClientException;
import com.example.demo.exception.ServerException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    //https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-webclient
    //https://medium.com/nerd-for-tech/webclient-error-handling-made-easy-4062dcf58c49
    //Exchange filter function is other way to handle webclient errors
        //https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-filter.html
    @Bean(name="employeeClient")
    public WebClient employeeClient(WebClient.Builder builder, EmployeeClientProperties employeeClientProperties) {
        return webClientConfig(builder, employeeClientProperties.getBaseUrl());
    }

    @Bean(name="departmentClient")
    public WebClient departmentClient(WebClient.Builder builder, DepartmentClientProperties departmentClientProperties) {
        return webClientConfig(builder, departmentClientProperties.getBaseUrl());
    }

    private WebClient webClientConfig(WebClient.Builder builder, String baseUrl){
        return builder
                .baseUrl(baseUrl)
                .filter(logRequest())
                .defaultStatusHandler(
                        httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ClientException((HttpStatus) response.statusCode(), body)))
                )
                .defaultStatusHandler(
                        httpStatusCode -> httpStatusCode.value() == HttpStatus.FORBIDDEN.value(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ClientException((HttpStatus) response.statusCode(), body)))
                )
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ServerException((HttpStatus) response.statusCode(), body)))
                )
                .build();
    }

    private ExchangeFilterFunction logRequest(){
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request:"+clientRequest.method()+" "+clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

}

@Data
@Component
//The official documentation advises that we isolate configuration properties into separate POJOs.
//https://www.baeldung.com/configuration-properties-in-spring-boot#simple-properties
//https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-vs-value
@ConfigurationProperties(prefix = "employee-service")
class EmployeeClientProperties {
    private String baseUrl;
}

@Data
@Component
@ConfigurationProperties(prefix = "department-service")
class DepartmentClientProperties {
    private String baseUrl;
}
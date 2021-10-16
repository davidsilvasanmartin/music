package blog.cybertricks.music.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // https://spring.io/guides/gs/gateway/
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        // TODO: how to include /api/v1 here automatically ?
                        .path("/api/v1/albums")
                        .uri("http://music-service/albums")
                )
                .route(p -> p
                        .path("/api/v1/songs")
                        .uri("http://music-service/songs")
                )
                .build();
    }
}

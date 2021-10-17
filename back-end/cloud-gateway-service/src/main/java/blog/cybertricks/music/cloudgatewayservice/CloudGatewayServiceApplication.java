package blog.cybertricks.music.cloudgatewayservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudGatewayServiceApplication {
    @Value("${base-path}")
    private String basePath;

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayServiceApplication.class, args);
    }

    // https://spring.io/guides/gs/gateway/
    // https://cloud.spring.io/spring-cloud-gateway/reference/html/
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("albumsBase", p -> p
                        .path(basePath + "/albums")
                        .filters(rw -> rw.rewritePath(basePath + "/albums", "/albums"))
                        .uri("lb://music-service/")
                )
                .route("albums", p -> p
                        .path(basePath + "/albums/**")
                        .filters(rw -> rw.rewritePath(basePath + "/albums/(?<segment>.*)", "/albums/${segment}"))
                        .uri("lb://music-service/")
                )
                .route("songsBase", p -> p
                        .path(basePath + "/songs")
                        .filters(rw -> rw.rewritePath(basePath + "/songs", "/songs"))
                        .uri("lb://music-service/")
                )
                .route("songs", p -> p
                        .path(basePath + "/songs/**")
                        .filters(rw -> rw.rewritePath(basePath + "/songs/(?<segment>.*)", "/songs/${segment}"))
                        .uri("lb://music-service/")
                )
                .build();
    }
}

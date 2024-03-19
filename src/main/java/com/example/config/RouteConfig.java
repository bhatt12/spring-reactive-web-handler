package com.example.config;

import com.example.handler.MovieReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> movieHandlerRoutes(MovieReviewHandler movieReviewHandler){

        return RouterFunctions.route()
                .nest(path("/v1/movie-review"), builder->{
                    builder.POST("", request-> movieReviewHandler.addReviews(request))
                            .GET("", request-> movieReviewHandler.getReviews(request))
                            .PUT("", request-> movieReviewHandler.updateReview(request))
                            .DELETE("", request -> movieReviewHandler.deleteReview(request));
                })
                .build();
    }
}

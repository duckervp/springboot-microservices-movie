package com.duckervn.streamservice.api.v1;

import com.duckervn.streamservice.api.v1.handlers.VideoRouteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Routes {

    @Bean
    RouterFunction<ServerResponse> videoEndPoint(VideoRouteHandler videoRouteHandler) {
        return route()
                .nest(
                        path("/files"), builder -> builder
                                .nest(path("/{name}"), videoBuilder -> videoBuilder
                                        .GET("", param("partial"), videoRouteHandler::getPartialContent)
                                        .GET("", videoRouteHandler::getFullContent)
                                )
                ).build();
    }

    private static RequestPredicate param(String parameter) {
        return RequestPredicates.all().and(request -> request.queryParam(parameter).isPresent());
    }

}

package com.duckervn.streamservice.api.v1.handlers;


import com.duckervn.streamservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class VideoRouteHandler {

    private final VideoService videoService;

    public Mono<ServerResponse> getPartialContent(ServerRequest request) {
        String name = request.pathVariable("name");
        Mono<ResourceRegion> resourceRegionMono = videoService.getRegion(name, request);

        return resourceRegionMono
                .flatMap(resourceRegion -> ServerResponse
                        .status(HttpStatus.PARTIAL_CONTENT)
                        .contentLength(resourceRegion.getCount())
                        .headers(headers -> headers.setCacheControl(CacheControl.noCache()))
                        .body(resourceRegionMono, ResourceRegion.class))
                .doOnError(throwable -> {
                    throw Exceptions.propagate(throwable);
                });
    }

    /**
     * This function gets a file from the file system and returns it as a whole
     * videoResource.contentLength() is a blocking call, therefore it is wrapped in a Mono.
     * it returns a FileNotFound exception which is wrapped and propagated down the stream
     */
    public Mono<ServerResponse> getFullContent(ServerRequest request) {

        String fileName = request.pathVariable("name");

        Mono<UrlResource> videoResourceMono = videoService.getResourceByName(fileName);


        return videoResourceMono
                .flatMap(urlResource -> {
                    long contentLength = videoService.lengthOf(urlResource);
                    return ServerResponse
                            .ok()
                            .contentLength(contentLength)
                            .headers(httpHeaders -> httpHeaders.setCacheControl(CacheControl.noCache()))
                            .body(videoResourceMono, UrlResource.class);
                });

    }

}

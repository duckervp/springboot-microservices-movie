package com.duckervn.streamservice.service;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface VideoService {

    Mono<ResourceRegion> getRegion(String name, ServerRequest request);

    Mono<UrlResource> getResourceByName(String name);

    long lengthOf(UrlResource urlResource);
}

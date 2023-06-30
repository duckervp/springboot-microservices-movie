package com.duckervn.streamservice.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileStorageService {
    void init();

    Mono<String> save(Mono<FilePart> filePartMono);

    Flux<DataBuffer> load(String filename);

    boolean delete(String filename);

    Stream<Path> loadAll();

    Path getRootPath();
}

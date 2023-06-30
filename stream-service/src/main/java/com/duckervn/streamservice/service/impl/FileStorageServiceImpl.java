package com.duckervn.streamservice.service.impl;

import com.duckervn.streamservice.config.ServiceConfig;
import com.duckervn.streamservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final ServiceConfig serviceConfig;

    @Override
    public void init() {
        try {
            Files.createDirectories(getRootPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Mono<String> save(Mono<FilePart> filePartMono) {
        return filePartMono.doOnNext(fp -> log.info("Receiving File:" + fp.filename())).flatMap(filePart -> {
            String filename = String.format("%s-%s", UUID.randomUUID(), filePart.filename());
            return filePart.transferTo(getRootPath().resolve(filename)).then(Mono.just(filename));
        });
    }

    @Override
    public Flux<DataBuffer> load(String filename) {
        try {
            Path file = getRootPath().resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.getRootPath(), 1)
                    .filter(path -> !path.equals(this.getRootPath()))
                    .map(this.getRootPath()::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = getRootPath().resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Path getRootPath() {
        return Paths.get(serviceConfig.getRootLocation());
    }
}

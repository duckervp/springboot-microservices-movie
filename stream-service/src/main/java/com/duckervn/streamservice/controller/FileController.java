package com.duckervn.streamservice.controller;

import com.duckervn.streamservice.common.RespMessage;
import com.duckervn.streamservice.common.Response;
import com.duckervn.streamservice.common.Utils;
import com.duckervn.streamservice.config.ServiceConfig;
import com.duckervn.streamservice.domain.model.output.FileInfo;
import com.duckervn.streamservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileStorageService storageService;

    private final ServiceConfig serviceConfig;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public Mono<ResponseEntity<Response>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return storageService.save(filePartMono).map(
                (filename) -> ResponseEntity.ok().body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(RespMessage.UPLOADED_FILE)
                        .result(new FileInfo(filename, Utils.getUrlFromFilename(serviceConfig.getGatewayUrl(), filename)))
                        .build()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping()
    public Mono<ResponseEntity<Flux<FileInfo>>> getListFiles() {

        Stream<FileInfo> fileInfoStream = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = Utils.getUrlFromFilename(serviceConfig.getGatewayUrl(), filename);
            return new FileInfo(filename, url);
        });

        Flux<FileInfo> fileInfosFlux = Flux.fromStream(fileInfoStream);

        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(fileInfosFlux));
    }

    @GetMapping("/img/{filename:.+}")
    public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable String filename, @RequestParam(required = false, defaultValue = "false") boolean download) {
        Flux<DataBuffer> file = storageService.load(filename);

        if (download) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(file);
        }

    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{filename:.+}")
    public Mono<ResponseEntity<Response>> deleteFile(@PathVariable String filename) {
        String message;

        try {
            boolean existed = storageService.delete(filename);

            if (existed) {
                message = RespMessage.DELETED_FILE;
                return Mono.just(ResponseEntity.ok().body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build()));
            }

            message = RespMessage.FILE_NOT_EXIST;
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(message)
                    .build()));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(message)
                    .build()));
        }
    }

}

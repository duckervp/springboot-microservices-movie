package com.duckervn.streamservice.controller;

import com.duckervn.streamservice.service.VideoStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class StreamingController {
    private final VideoStreamService videoStreamService;

    @GetMapping("/videos/{fileType}/{fileName}")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @PathVariable("fileType") String fileType,
                                                    @PathVariable("fileName") String fileName,
                                                    @RequestParam(required = false, defaultValue = "false") boolean dev) {
        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList, dev));
    }


    @GetMapping("/videos2/{fileType}/{fileName}")
    public Mono<Resource> streamVideo2(@RequestHeader(value = "Range", required = false) String range,
                                       @PathVariable("fileType") String fileType,
                                       @PathVariable("fileName") String fileName,
                                       @RequestParam(required = false, defaultValue = "false") boolean dev) {
        return videoStreamService.getResource(fileName, fileType, dev);
    }

}

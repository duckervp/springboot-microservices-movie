package com.duckervn.streamservice.service.impl;

import com.duckervn.streamservice.common.Constants;
import com.duckervn.streamservice.common.Response;
import com.duckervn.streamservice.common.TypeRef;
import com.duckervn.streamservice.common.Utils;
import com.duckervn.streamservice.config.ServiceConfig;
import com.duckervn.streamservice.domain.model.output.FileInfo;
import com.duckervn.streamservice.queue.EventProducer;
import com.duckervn.streamservice.service.CleanerService;
import com.duckervn.streamservice.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanerServiceImpl implements CleanerService {

    private final FileStorageService fileStorageService;

    private final ServiceConfig serviceConfig;

    private final EventProducer eventProducer;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Response clean() {
        List<FileInfo> fileInfos = new ArrayList<>();
        Stream<FileInfo> fileInfoStream = fileStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = Utils.getUrlFromFilename(serviceConfig.getGatewayUrl(), filename);
            return new FileInfo(filename, url);
        });

        fileInfoStream.forEach(fileInfos::add);

        List<String> storedFileUrls = new ArrayList<>();

        boolean isError = false;

        try {
            storedFileUrls.addAll(processGetUrls(
                    serviceConfig.getMovieTopic(),
                    serviceConfig.getMovieToStreamReplyTopic(),
                    serviceConfig.getFindMovieStoredFileEvent())
            );
            storedFileUrls.addAll(processGetUrls(
                    serviceConfig.getUserTopic(),
                    serviceConfig.getUserToStreamReplyTopic(),
                    serviceConfig.getFindUserStoredFileEvent())
            );
        } catch (Exception e) {
            log.info("Error: ", e);
            isError = true;
        }

        log.info("Stored file size: {}", storedFileUrls.size());

        List<String> deletedFileNames = new ArrayList<>();

        if (!isError) {
            for (FileInfo file : fileInfos) {
                if (!storedFileUrls.contains(file.getUrl())) {
                    fileStorageService.delete(file.getName());
                    deletedFileNames.add(file.getName());
                }
            }
        }

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message("Removed " + deletedFileNames.size() + " unused files!")
                .results(deletedFileNames)
                .build();
    }

    @Override
    public void downloadAllImages() throws InterruptedException {
        List<String> storedFileUrls = new ArrayList<>();

        try {
            storedFileUrls.addAll(processGetUrls(
                    serviceConfig.getMovieTopic(),
                    serviceConfig.getMovieToStreamReplyTopic(),
                    serviceConfig.getFindMovieStoredFileEvent())
            );
            storedFileUrls.addAll(processGetUrls(
                    serviceConfig.getUserTopic(),
                    serviceConfig.getUserToStreamReplyTopic(),
                    serviceConfig.getFindUserStoredFileEvent())
            );
        } catch (Exception e) {
            log.info("Error: ", e);
        }

        for (String url : storedFileUrls) {
            Utils.saveFileFromUrl(url);
            Thread.sleep(1000);
        }
    }


    private List<String> processGetUrls(String topic, String replyTopic, String event) {
        Map<String, Object> result = eventProducer.publishAndWait(topic, replyTopic, event, new HashMap<>());

        if (result.containsKey(Constants.DATA_ATTR)) {
            return objectMapper.convertValue(result.get(Constants.DATA_ATTR), TypeRef.LIST_STRING);
        }
        return new ArrayList<>();
    }
}

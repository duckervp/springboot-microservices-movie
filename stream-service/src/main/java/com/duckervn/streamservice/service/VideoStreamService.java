package com.duckervn.streamservice.service;

import com.duckervn.streamservice.common.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class VideoStreamService {
    @Value("${storage.local}")
    private String localStorage;


    private final Logger logger = LoggerFactory.getLogger(VideoStreamService.class);

    private final AmazonS3Service amazonS3Service;

    public Mono<Resource> getResource(final String fileName, final String fileType, boolean isLocalDev) {
        final String fileKey = fileName + "." + fileType;
        Mono<Resource> videoResource;
        if (isLocalDev) {
            videoResource = Mono.fromSupplier(() -> readFile(fileKey));
        } else {
            videoResource = Mono.fromSupplier(() -> amazonS3Service.getResource(fileKey));
        }
        return videoResource;
    }

    /**
     * Prepare the content.
     *
     * @param fileName String.
     * @param fileType String.
     * @param range    String.
     * @return ResponseEntity.
     */
    public ResponseEntity<byte[]> prepareContent(final String fileName, final String fileType, final String range, boolean isLocalDev) {

        try {
            final String fileKey = fileName + "." + fileType;
            long rangeStart = 0;
            long rangeEnd = Constants.CHUNK_SIZE;

            Resource videoResource;
            if (isLocalDev) {
                videoResource = readFile(fileKey);
            } else {
                videoResource = amazonS3Service.getResource(fileKey);
            }

            final Long fileSize = getFileSize(videoResource);

            if (range == null) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(Constants.CONTENT_TYPE, Constants.VIDEO_CONTENT + fileType)
                        .header(Constants.ACCEPT_RANGES, Constants.BYTES)
                        .header(Constants.CONTENT_LENGTH, String.valueOf(rangeEnd))
                        .header(Constants.CONTENT_RANGE, Constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        .header(Constants.CONNECTION, Constants.KEEP_ALIVE)
                        .header(Constants.CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(readByte(videoResource, rangeStart, rangeEnd)); // Read the object and convert it as bytes
            }

            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));

            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = rangeStart + Constants.CHUNK_SIZE;
            }

            rangeEnd = Math.min(rangeEnd, fileSize - 1);
            final byte[] data = readByte(videoResource, rangeStart, rangeEnd);
            final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;

            if (rangeEnd >= fileSize) {
                httpStatus = HttpStatus.OK;
            }

            return ResponseEntity.status(httpStatus)
                    .header(Constants.CONTENT_TYPE, Constants.VIDEO_CONTENT + fileType)
                    .header(Constants.ACCEPT_RANGES, Constants.BYTES)
                    .header(Constants.CONTENT_LENGTH, contentLength)
                    .header(Constants.CONNECTION, Constants.KEEP_ALIVE)
                    .header(Constants.CONTENT_RANGE, Constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                    .body(data);
        } catch (IOException e) {
            logger.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    public Resource readFile(String fileKey) {
        return new FileSystemResource(localStorage + fileKey);
    }

    public long getFileSize(Resource resource) throws IOException{
        return resource.contentLength();
    }

    public byte[] readByte(Resource resource, long start, long end) throws IOException {
        InputStream inputStream = resource.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] data = bufferedInputStream.readAllBytes();
        byte[] result = new byte[(int) (end - start) + 1];
        System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
        return result;
    }

}

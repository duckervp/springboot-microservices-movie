package com.duckervn.streamservice.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Utils {
    public static String getUrlFromFilename(String gatewayUrl, String filename) {
        return UriComponentsBuilder.newInstance()
                .path("{gatewayUrl}/files/{filename}")
                .buildAndExpand(gatewayUrl, filename)
                .toUriString();
    }

    public static void saveFileFromUrl(String url) {
        try(InputStream in = new URL(url).openStream()){
            Files.copy(in, Paths.get("C:\\Users\\ductr\\Downloads\\files\\" + UUID.randomUUID() + ".jpg"));
        } catch (IOException ignored) {
        }
    }

    public static String objectToString(Object o) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }

}

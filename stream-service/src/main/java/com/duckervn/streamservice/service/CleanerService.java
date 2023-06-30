package com.duckervn.streamservice.service;

import com.duckervn.streamservice.common.Response;

public interface CleanerService {
    Response clean();

    void downloadAllImages() throws InterruptedException;
}

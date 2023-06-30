package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IFileStoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FileStoreService implements IFileStoreService {

    private final UserRepository userRepository;

    @Override
    public Set<String> getStoredImageUrls() {
        return new HashSet<>(userRepository.findUserImageUrls())
                .stream().filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }
}

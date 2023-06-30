package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.dto.MovieImageDTO;
import com.duckervn.movieservice.repository.CharacterRepository;
import com.duckervn.movieservice.repository.EpisodeRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final CharacterRepository characterRepository;

    private final EpisodeRepository episodeRepository;

    private final MovieRepository movieRepository;

    @Override
    public Set<String> getAllStoredFiles() {
        List<String> urls = new ArrayList<>();

        urls.addAll(characterRepository.findAvatarUrls());

        urls.addAll(episodeRepository.findEpisodeMovieUrls());

        List<MovieImageDTO> movieImageUrls = movieRepository.findMovieImageUrls();

        for (MovieImageDTO movieImageDTO : movieImageUrls) {
            urls.add(movieImageDTO.getBannerUrl());
            urls.add(movieImageDTO.getPosterUrl());
        }

        return new HashSet<>(urls).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }
}

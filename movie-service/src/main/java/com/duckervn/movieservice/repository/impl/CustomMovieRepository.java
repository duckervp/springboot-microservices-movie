package com.duckervn.movieservice.repository.impl;

import com.duckervn.movieservice.domain.model.findmovie.MovieOutput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.repository.ICustomMovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CustomMovieRepository implements ICustomMovieRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param name        movie name
     * @param releaseYear release year
     * @param country     country
     * @param genre     genre
     * @param pageable    paging info
     * @return list of movie after paging
     */
    @Override
    public PageOutput<?> findMovieOutput(String name, Integer releaseYear, String country, String genre, Pageable pageable) {
        Assert.isTrue(pageable.getPageNumber() > 0, "pageNo is greater than zero");
        Assert.isTrue(pageable.getPageSize() > 0, "pageSize is greater than zero");
        StringBuilder countQueryString = new StringBuilder();
        StringBuilder findQueryString = new StringBuilder();
        StringBuilder conditions = new StringBuilder();
        Map<String, Object> mapping = new HashMap<>();

        countQueryString.append("SELECT COUNT(m.id) FROM movie AS m ");
        findQueryString.append("SELECT m.id, m.name, m.release_year AS releaseYear, ");
        findQueryString.append("m.total_episode AS totalEpisode, m.country, m.banner_url AS bannerUrl, ");
        findQueryString.append("m.poster_url AS posterUrl, m.description, m.created_at AS createdAt, ");
        findQueryString.append("m.modified_at AS modifiedAt, m.slug AS slug FROM movie AS m ");

        if (Objects.nonNull(genre)) {
            conditions.append("INNER JOIN movie_genre AS mg ON m.id = mg.movie_id ")
                    .append("INNER JOIN genre AS g ON g.id = mg.genre_id ")
                    .append("WHERE g.slug = :genre ");
            mapping.put("genre", genre);
        } else {
            conditions.append("WHERE 1=1 ");
        }
        if (Objects.nonNull(name)) {
            conditions.append("AND name LIKE :name ");
            mapping.put("name", name);
        }
        if (Objects.nonNull(releaseYear)) {
            conditions.append("AND release_year = :releaseYear ");
            mapping.put("releaseYear", releaseYear);
        }
        if (Objects.nonNull(country)) {
            conditions.append("AND country = :country ");
            mapping.put("country", country);
        }

        countQueryString.append(conditions);
        findQueryString.append(conditions);

        // paging
        findQueryString.append("LIMIT ")
                .append(pageable.getPageSize())
                .append(" OFFSET ")
                .append(pageable.getPageSize() * (pageable.getPageNumber() - 1));

        Query countQuery = entityManager.createNativeQuery(countQueryString.toString());
        Query findQuery = entityManager.createNativeQuery(findQueryString.toString());

        mapping.forEach(countQuery::setParameter);
        mapping.forEach(findQuery::setParameter);

        long totalElements = ((BigInteger) countQuery.getSingleResult()).longValue();

        if (pageable.getPageNumber() > totalElements / pageable.getPageSize() + 1) {
            // page number get over total page
            return new PageOutput<>(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize(), totalElements);

        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = findQuery.getResultList();
        if (CollectionUtils.isEmpty(results)) {
            return new PageOutput<>(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize(), totalElements);
        }

        List<?> movieOutputs = results.stream()
                .map(objects -> MovieOutput.builder()
                        .id(toLong(objects[0]))
                        .name((String) objects[1])
                        .releaseYear((Integer) objects[2])
                        .totalEpisode((Integer) objects[3])
                        .country((String) objects[4])
                        .bannerUrl((String) objects[5])
                        .posterUrl((String) objects[6])
                        .description((String) objects[7])
                        .createdAt(toLocalDateTime(objects[8]))
                        .modifiedAt(toLocalDateTime(objects[9]))
                        .slug((String) objects[10])
                        .build())
                .collect(Collectors.toList());
        return new PageOutput<>(movieOutputs, pageable.getPageNumber(), pageable.getPageSize(), totalElements);
    }

    /**
     * Convert Sql Timestamp object to LocalDateTime
     *
     * @param object Object instance of Timestamp
     * @return LocalDateTime value
     */
    private LocalDateTime toLocalDateTime(Object object) {
        if (Objects.nonNull(object) && object instanceof Timestamp) {
            return ((Timestamp) object).toLocalDateTime();
        }
        return null;
    }

    /**
     * Convert object BigInteger to Long
     *
     * @param object Object instance of BigInteger
     * @return Long value
     */
    private Long toLong(Object object) {
        if (Objects.nonNull(object) && object instanceof BigInteger) {
            return ((BigInteger) object).longValue();
        }
        return null;
    }
}

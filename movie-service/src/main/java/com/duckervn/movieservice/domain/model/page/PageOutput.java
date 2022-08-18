package com.duckervn.movieservice.domain.model.page;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageOutput<T> {
    List<T> content;

    private Integer pageNo;

    private Integer pageSize;

    private Long totalElements;
}

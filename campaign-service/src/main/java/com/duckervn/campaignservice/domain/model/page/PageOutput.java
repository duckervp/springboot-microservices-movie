package com.duckervn.campaignservice.domain.model.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageOutput<T> implements Serializable {
    List<T> content;

    private Integer pageNo;

    private Integer pageSize;

    private Long totalElements;
}

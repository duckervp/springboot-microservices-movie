package com.duckervn.movieservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int code;

    private String message;

    private Object result;

    private List<Object> results;

    private Integer pageSize;

    private Integer pageNo;

    private Long totalElements;
}

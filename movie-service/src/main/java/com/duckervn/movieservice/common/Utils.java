package com.duckervn.movieservice.common;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static List<Object> toObjectList(List<?> list) {
        return list.stream().map(item -> (Object) item).collect(Collectors.toList());
    }
}

package com.duckervn.movieservice.common;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class TypeRef {

    public static final TypeReference<List<String>> LIST_STRING = new TypeReference<>(){};

    public static final TypeReference<Map<String, String>> MAP_STRING_STRING = new TypeReference<>(){};

    public static final TypeReference<Map<String, Object>> MAP_STRING_OBJECT = new TypeReference<>(){};


}

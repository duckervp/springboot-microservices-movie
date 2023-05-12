package com.duckervn.movieservice.common;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public static List<Object> toObjectList(List<?> list) {
        return list.stream().map(item -> (Object) item).collect(Collectors.toList());
    }

    private static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String genSlug(String text) {
        return String.join("-", Utils.deAccent(text).toLowerCase().split("\\s+"));
    }
}

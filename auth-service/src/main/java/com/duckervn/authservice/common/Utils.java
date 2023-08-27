package com.duckervn.authservice.common;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, Object> genResetPasswordToken(String input, long validityInMinute) {
        Map<String, Object> tokenObject = new HashMap<>();
        String secret = genSecret(input);
        String token = Base64.getEncoder().encodeToString(secret.getBytes());
        tokenObject.put(Constants.TOKEN, token);
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(validityInMinute);
        Timestamp timestamp = Timestamp.valueOf(expiredAt);
        tokenObject.put(Constants.EXPIRED_AT, timestamp.getTime());
        return tokenObject;
    }

    public static String extractBase64Token(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }

    public static String genSecret(String input) {
        String salt = DigestUtils.md5Hex("tH!s1sS@1t").toUpperCase();
        return DigestUtils.md5Hex(input + salt).toUpperCase();
    }

    public static LocalDate convertStringToLocalDate(String localDateString) {
        List<Integer> dobs = Arrays.stream(localDateString.split("[/-:]"))
                .map(Integer::parseInt).collect(Collectors.toList());
        return LocalDate.of(dobs.get(2), dobs.get(1), dobs.get(0));
    }
}

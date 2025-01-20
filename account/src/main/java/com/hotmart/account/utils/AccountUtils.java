package com.hotmart.account.utils;

import lombok.NonNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AccountUtils {

    public static <T> Consumer<Set<T>> listToConsumer(List<T> list) {
        Consumer<Set<T>> setConsumer = set -> {
            set.forEach(System.out::println);
        };

        Set<T> set = new HashSet<>(list);
        setConsumer.accept(set);
        return setConsumer;
    }

    public static List<ClientAuthenticationMethod> convertToClientAuthenticationMethod(List<String> values) {
        return values.stream()
                .map(ClientAuthenticationMethod::new)
                .collect(Collectors.toList());
    }

    public static List<AuthorizationGrantType> convertToAuthorizationGrantType(List<String> values) {
        return values.stream()
                .map(AuthorizationGrantType::new)
                .collect(Collectors.toList());
    }

    public static String accentRemover(@NonNull String text) {
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
                .matcher(Normalizer.normalize(text, Normalizer.Form.NFD))
                .replaceAll("");
    }

    public static String generatedPassword(String name) {
        var nameSeparate = name.split(" ");
        return AccountUtils.accentRemover(nameSeparate.length > 1 ?
                        String.format("@A1b2c3_%s_%s", nameSeparate[0], nameSeparate[1]) :
                        String.format("@A1b2c3_%s", nameSeparate[0]));
    }

}

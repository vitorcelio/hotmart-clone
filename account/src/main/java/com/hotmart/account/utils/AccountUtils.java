package com.hotmart.account.utils;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
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

}

package com.hotmart.auth.utils;

import com.hotmart.auth.enums.Roles;

public class AuthUtils {

    public static final String TOKEN_ISSUER = "http://localhost:8080";

    public static final String TOKEN_AUDIENCE = "hotmartApp";

    public static final String OAUTH_AUTHORIZE_BUYER = "/oauth2/authorize?response_type=code&client_id=hotmartApp" +
            "&state=abc&redirect_uri=https://oidcdebugger.com/debug&scope=user_read profile_read";

    public static Roles roleService(String service) {
        if (service.contains("app.hotmart")) {
            return Roles.SELLER;
        } else if (service.contains("consumer.hotmart")) {
            return Roles.BUYER;
        }

        return null;
    }

    public static boolean hasXCharacters(String input, Integer howMany) {
        if (input == null || input.isEmpty()) {
            return true;
        }

        return howMany.equals(input.length());
    }

    public static String hidePartOfTheText(String text) {
        if (text == null || text.isEmpty()) {
            return "Não informado";
        }

        String regex = "^(\\w{4})(\\w*)(\\W.*)?$";
        return text.replaceAll(regex, "$1*****$3");
    }

}

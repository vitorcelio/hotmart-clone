package com.hotmart.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredClientRequestDTO {

    private String id;
    private String name;
    private String description;
    private String clientId;
    private String clientSecret;
    private List<String> clientAuthenticationMethod;
    private List<String> authorizationGrantType;
    private List<String> redirectUri;
    private List<String> scopes;

}

package com.example.blog.client;

import com.example.blog.dto.response.ExchangeGoogleTokenResponse;
import com.example.blog.dto.response.Oauth2UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticateGgService {

    @Value("${outbound.identity.client-id}")
    private String clientId;

    @Value("${outbound.identity.client-secret}")
    private String clientSecret;

    @Value("${outbound.identity.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    public ExchangeGoogleTokenResponse exchangeGoogleToken(String code){
        String url = "https://oauth2.googleapis.com/token";

        // Chuyển ExchangeTokenRequest thành MultiValueMap
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");

        // Cấu hình header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo HttpEntity với map dữ liệu và header
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        // Gửi request và nhận response đồng bộ
        return restTemplate.exchange(
                url, HttpMethod.POST, entity, ExchangeGoogleTokenResponse.class
        ).getBody(); // Trả về body của response
    }

    public Oauth2UserResponse getUserInfo(String alt, String accessToken) {
        String url = "https://openidconnect.googleapis.com/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Oauth2UserResponse.class
        ).getBody();
    }
}

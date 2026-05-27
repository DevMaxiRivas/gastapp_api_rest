package com.app.features.controller;

import com.app.config.AbstractIntegrationTest;
import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.AccessTokenResponse;
import com.app.dto.v1.auth.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.LinkedHashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    public final String PREFIX_URI = "/api/v1/auth";
    public final String ENDPOINT_REGISTER = "/register";
    public final String ENDPOINT_LOGIN = "/login";
    public final String ENDPOINT_LOGOUT = "/logout";
    public final String ENDPOINT_REFRESH = "/refresh";


    @Test
    void registerUserTest() {
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser1",
                    "email" : "test1@example.com",
                    "currency" : "USD",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus().isCreated()
                .expectCookie().exists("refresh-token")
                .expectBody()
                    .jsonPath("$.data.access_token").exists()
        ;

        // Testing Error Enum CurrencyType
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser",
                    "email" : "test@example.com",
                    "currency"  : "UD",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus()
                    .isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                    .jsonPath("$.errors").exists()
                    .jsonPath("$.status").isEqualTo("error");
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser",
                    "email" : "test@example.com",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus()
                    .is4xxClientError()
                .expectBody()
                    .jsonPath("$.errors").exists()
                    .jsonPath("$.status").isEqualTo("error")
        ;
    }

    @Test
    void authEndpointsTest() {
        EntityExchangeResult<ApiResponse<AccessTokenResponse>> resultRegister = webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser",
                    "email" : "test@example.com",
                    "currency" : "USD",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectCookie()
                    .exists("refresh-token")
                .expectBody(
                        new ParameterizedTypeReference<ApiResponse<AccessTokenResponse>>() {}
                )
                .returnResult()
            ;

        AccessTokenResponse token = resultRegister.getResponseBody().getData();
        ResponseCookie refreshTokenCookie = resultRegister.getResponseCookies().getFirst("refresh-token");
        String refreshToken = null;
        if (refreshTokenCookie != null) {
            refreshToken = refreshTokenCookie.getValue();
        }

        webTestClient.get()
                .uri("/api/v1/users")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token.access_token())
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                .consumeWith(result -> {
                    System.out.println("Endpoint users response Body: " + new String(result.getResponseBodyContent()));
                });
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_LOGOUT)
                .header("Content-Type", "application/json")
                .header("Cookie", "refresh-token=" + refreshToken)
                .exchange()
                .expectStatus()
                    .is2xxSuccessful()
                .expectCookie()
                    .exists("refresh-token")
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_LOGIN)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "email" : "test@example.com",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus()
                    .is2xxSuccessful()
                .expectCookie()
                    .exists("refresh-token")
                .expectBody()
                    .jsonPath("$.data.access_token").exists();
    }

}

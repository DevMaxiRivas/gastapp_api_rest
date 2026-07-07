package com.app.features.controller.auth;

import com.app.config.AbstractIntegrationTest;
import com.app.config.DotenvInitializer;
import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.AccessTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

// Comment out this statement if you are implementing CI/CD
// Configuration class that loads environment variables
@ContextConfiguration(initializers = DotenvInitializer.class)
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
    void registerTest() {
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                            "username" : "TestUser1",
                            "email" : "maximiliano.rivas.work@gmail.com",
                            "password" : "Pwd#12345"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectCookie().exists("refresh-token")
                .expectBody()
                .jsonPath("$.data.access_token").exists()
        ;
    }


    @Test
    void registerUserTest() {
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "username" : "TestUser1",
                    "email" : "test1@example.com",
                    "password" : "Pwd#12345"
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
                    "username" : "TestUser",
                    "email" : "test@example.com",
                    "password" : "Pwd12345",
                }
                """)
                .exchange()
                .expectStatus()
                    .isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                    .jsonPath("$.errors").exists()
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "username" : "TestUser",
                    "email" : "test@example.com",
                    "password" : "Pwd12345",
                }
                """)
                .exchange()
                .expectStatus()
                    .is4xxClientError()
                .expectBody()
                    .jsonPath("$.errors").exists()
                    .jsonPath("$.success").isEqualTo(false)
        ;
    }

    @Test
    void authEndpointsTest() {
        EntityExchangeResult<ApiResponse<AccessTokenResponse>> resultRegister = webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "username" : "TestUser",
                    "email" : "test@example.com",
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

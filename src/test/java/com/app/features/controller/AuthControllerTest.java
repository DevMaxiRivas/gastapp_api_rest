package com.app.features.controller;

import com.app.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    public final String PREFIX_URI = "/api/v1/auth";
    public final String ENDPOINT_REGISTER = "/register";
    public final String ENDPOINT_LOGIN = "/login";


    @Test
    void registerUserTest() {
        webTestClient.post()
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
                .expectStatus().isCreated()
                .expectCookie().exists("refresh-token")
                .expectBody()
                    .jsonPath("$.data.token").exists()
        ;

        // Testing Error Enum CurrencyType
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser",
                    "email" : "test@example.com",
                    "currency" : "UD",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
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
    void loginUserTest() {
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
                .expectStatus().isCreated()
                .expectCookie().exists("refresh-token")
                .expectBody()
                .jsonPath("$.data.token").exists()
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_LOGIN)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "email" : "test@example.com",
                    "password" : "Pwd12345",
                }
                """)
                .exchange()
                .expectStatus()
                    .isBadRequest()
                .expectBody()
                    .jsonPath("$.status").isEqualTo("error");
        ;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_LOGIN)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "email" : "test@example.com",
                }
                """)
                .exchange()
                .expectStatus()
                    .is4xxClientError()
                .expectBody()
                    .jsonPath("$.status").isEqualTo("error");
        ;
    }

}

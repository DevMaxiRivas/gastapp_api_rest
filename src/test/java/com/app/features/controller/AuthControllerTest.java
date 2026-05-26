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

    @Test
    void registerUserTest() {
        webTestClient.post()
                .uri("/api/v1/auth/register")
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
                .expectBody()
                    .jsonPath("$.data.token").exists()
                    .jsonPath("$.data.name").isEqualTo("TestUser")
                    .jsonPath("$.data.email").isEqualTo("test@example.com")
        ;

        // Testing Error Enum CurrencyType
        webTestClient.post()
                .uri("/api/v1/auth/register")
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
                .uri("/api/v1/auth/register")
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "name" : "TestUser",
                    "email" : "test@example.com",
                    "password" : "Pwd12345"
                }
                """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.status").isEqualTo("error")
        ;


//        // 2. Probar el GET (Persistencia real en Postgres)
//        webTestClient.get()
//                .uri("/api/usuarios")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(Object.class).hasSize(1);
    }
}

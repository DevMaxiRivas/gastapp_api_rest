package com.app.features.controller.transaction;

import com.app.config.AbstractIntegrationTest;
import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.AccessTokenResponse;
import com.app.dto.v1.auth.RegisterRequest;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CreateTransactionTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private String rawBodyRegister;

    public final String PREFIX_URI = "/api/v1";
    public final String ENDPOINT_REGISTER = "/auth/register";
    public final String ENDPOINT_TEST = "/transactions";

    private void setBodyRegister(){
        RegisterRequest userDto = new RegisterRequest(
                "johndoe",
                "john.doe@example.com",
                "Pwd##123"
        );
        this.rawBodyRegister = String.format(
                """
                {
                    "username" : "%s",
                    "email" : "%s",
                    "password" : "%s"
                }
                """,
                userDto.username(),
                userDto.email(),
                userDto.password()
        );
    }

    @BeforeEach
    void setUp() {
        setBodyRegister();
    }

    @Test
    void createTransactionHappyPath() {
        EntityExchangeResult<ApiResponse<AccessTokenResponse>> resultRegister = webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue(rawBodyRegister)
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

        AccessTokenResponse response = resultRegister.getResponseBody().getData();
        String transactionJson = """
            {
                "amount" : 2000,
                "type" : "EXPENSE",
                "transactionDate" : "2026-06-08",
                "note" : "Test Shop",
                "categoryId" : 1
            }
            """;

        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_TEST)
                .header("Authorization", "Bearer " + response.access_token())
                .header("Content-Type", "application/json")
                .bodyValue(transactionJson)
                .exchange()
                .expectStatus().isCreated();
    }
}


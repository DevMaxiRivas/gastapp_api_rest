package com.app.features.controller.auth;

import com.app.config.AbstractIntegrationTest;
import com.app.config.DotenvInitializer;
import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.AccessTokenResponse;
import com.app.dto.v1.email.SendEmailDTO;
import com.app.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.mockito.ArgumentCaptor;
import static org.assertj.core.api.Assertions.assertThat;

// Comment out this statement if you are implementing CI/CD
// Configuration class that loads environment variables
@ContextConfiguration(initializers = DotenvInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EmailService emailService;

    public final String PREFIX_URI = "/api/v1/auth";
    public final String ENDPOINT_REGISTER = "/register";
    public final String ENDPOINT_LOGIN = "/login";
    public final String ENDPOINT_LOGOUT = "/logout";
    public final String ENDPOINT_REFRESH = "/refresh";

    @Test
    void registerUserHappyPathTest() {
        webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                    {
                        "username" : "TestUser1",
                        "email" : "test@example.com",
                        "password" : "Pwd#12345"
                    }
                    """)
                .exchange()
                .expectStatus().isCreated()
                .expectCookie().exists("refresh-token")
                .expectBody()
                .jsonPath("$.data.access_token").exists();

        ArgumentCaptor<SendEmailDTO> emailCaptor = ArgumentCaptor.forClass(SendEmailDTO.class);

        Mockito.verify(emailService, Mockito.timeout(2000))
                .sendEmail(
                        emailCaptor.capture(),           // We captured the DTO
                        ArgumentMatchers.eq("email/auth/register"), // Template Check
                        ArgumentMatchers.any()           // Context for thymeleaf
                );

        SendEmailDTO emailSent = emailCaptor.getValue();
        System.out.println(emailSent);
        assertThat(emailSent.to()).isEqualTo("test@example.com");
        assertThat(emailSent.subject()).isEqualTo("You just signed up");
    }


    @Test
    void registerUserWithBodyProblemTest() {
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
    }

    @Test
    void authEndpointsTest() {
        EntityExchangeResult<ApiResponse<AccessTokenResponse>> resultRegister = webTestClient.post()
                .uri(PREFIX_URI + ENDPOINT_REGISTER)
                .header("Content-Type", "application/json")
                .bodyValue("""
                {
                    "username" : "TestUser",
                    "email" : "test_2@example.com",
                    "password" : "Pwd#12345"
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
                    "password" : "Pwd#12345"
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

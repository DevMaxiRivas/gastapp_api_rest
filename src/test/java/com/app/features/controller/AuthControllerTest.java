package com.app.features.controller;

import com.app.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void debeGuardarYConsultarUnUsuario() {
        // Objeto de prueba
        String nuevoUsuarioJson = """
                {
                    "name" : "Maxi",
                    "email" : "algo@ex.com",
                    "currency" : "USD",
                    "password" : "Pwd12345"
                }
                """;

        // 1. Probar el POST (Creación)
        webTestClient.post()
                .uri("/api/auth")
                .header("Content-Type", "application/json")
                .bodyValue(nuevoUsuarioJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.nombre").isEqualTo("Juan Perez");

        // 2. Probar el GET (Persistencia real en Postgres)
        webTestClient.get()
                .uri("/api/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class).hasSize(1);
    }
}

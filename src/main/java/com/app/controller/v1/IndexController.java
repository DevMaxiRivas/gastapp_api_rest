package com.app.controller.v1;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<Void> restRedirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/swagger-ui/index.html"));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
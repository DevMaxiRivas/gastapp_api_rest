package com.app.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/greet")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hola Spring Boot";
    }
}

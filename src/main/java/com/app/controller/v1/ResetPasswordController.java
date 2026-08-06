package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.ConfirmResetPasswordRequest;
import com.app.dto.v1.auth.ResetPasswordRequest;
import com.app.service.auth.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService service;

    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> validateToken(@RequestParam("token") String token) {
        service.validateToken(token, false);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid  @RequestBody ConfirmResetPasswordRequest request) {
        service.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ResetPasswordRequest request){
        service.forgotPassword(request.email());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

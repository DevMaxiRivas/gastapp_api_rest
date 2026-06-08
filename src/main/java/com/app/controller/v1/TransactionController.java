package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.model.User;
import com.app.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {
     private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> createTransaction(
            @Valid @RequestBody TransactionCreateDTO dto,
            @AuthenticationPrincipal User user
    ) {
        TransactionResponseDTO result = transactionService.create(dto, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("transactions/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ApiResponse.created(result));
    }

}

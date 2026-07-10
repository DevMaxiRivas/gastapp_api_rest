package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.transaction.QueryParamsTransactionFilterDTO;
import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.dto.v1.transaction.TransactionUpdateDTO;
import com.app.model.User;
import com.app.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {
     private TransactionService service;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> createTransaction(
            @Valid @RequestBody TransactionCreateDTO dto,
            @AuthenticationPrincipal User user
    ) {
        TransactionResponseDTO result = service.create(dto, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("transactions/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ApiResponse.created(result));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@securityService.canAccessTransaction(#id)")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok(
                        ApiResponse.success(
                                service.update(id, dto, user)
                        )
                );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.canAccessTransaction(#id)")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getTransactions(
            @AuthenticationPrincipal User user,
            @Valid QueryParamsTransactionFilterDTO filters,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return ResponseEntity.ok(
                ApiResponse.paginatedSuccess(
                        service.getFilteredPageable(filters, pageable, user)
                )
        );
    }

}

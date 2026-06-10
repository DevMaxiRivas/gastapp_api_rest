package com.app.service.transaction.impl;

import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.exception.resource.ResourceNotFoundCustomException;
import com.app.mapper.transaction.TransactionMapper;
import com.app.model.Category;
import com.app.model.Transaction;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.repository.TransactionRepository;
import com.app.service.transaction.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    private final TransactionMapper mapper;

    @Override
    @Transactional
    public TransactionResponseDTO create(TransactionCreateDTO dto, User user) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ValidationRequestBodyCustomException("categoryId: is invalid.", "body.categoryId"));
        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(dto.amount());
        transaction.setNote(dto.note());
        transaction.setTransactionDate(dto.transactionDate());
        transaction.setType(dto.type());
        transactionRepository.save(transaction);
        return mapper.toDto(transaction);
    }

    @Override
    public List<Transaction> getMonthlyTransactions(Long userId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundCustomException("Transaction not found", "url");
        }
        transactionRepository.deleteById(id);
    }
}

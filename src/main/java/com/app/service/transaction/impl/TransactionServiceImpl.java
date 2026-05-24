package com.app.service.transaction.impl;

import com.app.model.Category;
import com.app.model.Transaction;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.repository.TransactionRepository;
import com.app.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Transaction create(Transaction transaction, Long userId, Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no existe"));

        transaction.setUser(user);
        transaction.setCategory(category);

        // Business Logic Here. Example: Send Notification

        return transactionRepository.save(transaction);
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
            throw new RuntimeException("La transacción no existe");
        }
        transactionRepository.deleteById(id);
    }
}

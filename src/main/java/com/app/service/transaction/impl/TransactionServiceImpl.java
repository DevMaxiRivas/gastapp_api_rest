package com.app.service.transaction.impl;

import com.app.dto.v1.dashboard.TransactionHistoryByMonthDTO;
import com.app.dto.v1.transaction.QueryParamsTransactionFilterDTO;
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
import com.app.service.category.CategoryService;
import com.app.service.transaction.TransactionService;
import com.app.specification.transaction.TransactionSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repo;
    private final CategoryService categoryService;
    private final TransactionMapper mapper;

    @Override
    @Transactional
    public TransactionResponseDTO create(TransactionCreateDTO dto, User user) {
        Category category = categoryService.findById(dto.categoryId())
                .orElseThrow(() -> new ValidationRequestBodyCustomException("categoryId: is invalid.", "body.categoryId"));

        Transaction transaction = mapper.toEntity(dto);
        transaction.setUser(user);
        transaction.setCategory(category);
        repo.save(transaction);
        return mapper.toDto(transaction);
    }

    @Override
    public List<Transaction> getMonthlyTransactions(Long userId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return repo.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundCustomException("Transaction not found", "url");
        }
        repo.deleteById(id);
    }

    @Override
    public Page<TransactionResponseDTO> getFilteredPageable(QueryParamsTransactionFilterDTO filters, Pageable pageable, User user){
        Long userId = (Objects.equals(user.getRole().getName(),"USER")) ? user.getId() : filters.userId();

        Specification<Transaction> spec = TransactionSpecification.filterBy(filters, userId);
        return repo.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    public List<TransactionResponseDTO> getRecentTransactions(Long userId, int quantity) {
        Limit limit = Limit.of(quantity);
        Sort sort = Sort.by(Sort.Direction.DESC, "transactionDate");

        return repo.findByUserId(userId, sort, limit)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionHistoryByMonthDTO> getTransactionHistoryByMonth(Long userId, LocalDate startDate, LocalDate endDate) {
        return repo.getTransactionHistoryByMonth(userId, startDate, endDate);
    }
}

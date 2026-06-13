package com.app.db.seeder.transaction;

import com.app.model.Category;
import com.app.model.Transaction;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.repository.TransactionRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class TransactionSeeder implements CommandLineRunner {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Value("${app.seed.enabled}")
    private boolean enabled;

    @Value("${app.seed.transactions.quantity}")
    private String seedTransactionsQuantity;

    private final int defaultQuantity = 20;

    private final long minAmount = 1000;
    private final long maxAmount = 100000;

    private final long maxDays = 60;

    @Override
    public void run(String... args) {
        if(!enabled) {
            log.info("Skipping seeder because are disabled");
            return;
        }

        if(transactionRepository.count() > 0) {
            log.info("Transactions have been seeded");
            return;
        }

        int quantityToCreate = seedTransactionsQuantity != null ? Integer.parseInt(seedTransactionsQuantity) : defaultQuantity;
        List<User> users = userRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        if(users.isEmpty() || categories.isEmpty())  {
            log.info("No users or categories have been created");
            return;
        }

        Faker faker = new Faker(new Locale("en"));

        for (int i = 0; i < quantityToCreate; i++) {
            Category category = categories.get((int)Math.round(Math.random() * (categories.size() - 1)));
            Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(Math.round(minAmount + Math.random()*(maxAmount - minAmount + 1 ))))
                .category(category)
                .user(users.get((int)Math.round(Math.random() * (users.size() - 1))))
                .type(category.getType())
                .note(faker.lorem().sentence())
                .transactionDate(LocalDate.now().minusDays(Math.round(Math.random() * maxDays)))
                .build();

            transactionRepository.save(transaction);
        }
        log.info("Seed completed: {} transaction created", quantityToCreate);
    }
}
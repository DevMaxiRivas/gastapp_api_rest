package com.app.db.seeder.user;

import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

/**
 * Seeds the database with realistic fake data for local development.
 *
 * <p>Only runs when the {@code dev} profile is active and
 * {@code app.seed.enabled=true}. Should never run in production.</p>
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled}")
    private boolean enabled;

    @Value("${app.seed.users.quantity}")
    private String seedUsersQuantity;

    private final int defaultQuantity = 20;

    @Override
    public void run(String... args) {
        if(!enabled) {
            log.info("Skipping seeder because are disabled");
            return;
        }

        if (userRepository.count() > 0) {
            log.info("Skipping seed: users table already has data");
            return;
        }

        int quantityToCreate = (seedUsersQuantity != null) ? Integer.parseInt(seedUsersQuantity) : defaultQuantity;

        Faker faker = new Faker(new Locale("en"));

        for (int i = 0; i < quantityToCreate; i++) {
            User user = User.builder()
                    .username(faker.internet().username())
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode("Pwd12345"))
                    .tokens(List.of())
                    .role(roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found")))
                    .build();

            userRepository.save(user);
        }

        log.info("Seed completed: {} users created", quantityToCreate);
    }
}
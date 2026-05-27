package com.app.repository;
import com.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(
            value = "SELECT COUNT(users.id) > 0 FROM users WHERE id = :userId AND :tokenHash = ANY(tokens)",
            nativeQuery = true)
    boolean isTokenValid(@Param("userId") Long userId, @Param("tokenHash") String tokenHash);

    @Modifying
    @Query(value = "UPDATE users SET tokens = array_remove(tokens, :tokenHash) WHERE id = :userId",
            nativeQuery = true)
    void removeToken(@Param("userId") Long userId, @Param("tokenHash") String tokenHash);

    @Modifying
    @Query(value = "UPDATE users SET tokens = '{}' WHERE id = :userId",
            nativeQuery = true)
    void removeAllTokens(@Param("userId") Long userId);
}

package enat.bank.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepo extends JpaRepository<UserLogging ,Long > {
    Optional<UserLogging> findByToken(String token);
}

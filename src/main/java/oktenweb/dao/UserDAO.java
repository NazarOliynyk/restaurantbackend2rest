package oktenweb.dao;

import oktenweb.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

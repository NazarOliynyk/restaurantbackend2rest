package oktenweb.services;

import oktenweb.models.ResponseURL;
import oktenweb.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    String save(User user);

    // User findByUsername(String username);

    List<User> findAll();

    User findOneById(Integer id);

    String deleteById(int id);
}

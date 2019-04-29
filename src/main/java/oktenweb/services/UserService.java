package oktenweb.services;


import oktenweb.models.ResponseTransfer;
import oktenweb.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseTransfer save(User user);

    List<User> findAll();

    User findOneById(Integer id);

    ResponseTransfer deleteById(int id);
}

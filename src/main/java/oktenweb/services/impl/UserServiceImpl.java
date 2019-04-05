package oktenweb.services.impl;

import oktenweb.dao.UserDAO;
import oktenweb.models.ResponseURL;
import oktenweb.models.User;
import oktenweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseURL save(User user) {
        ResponseURL responseURL ;
        if (user.getUsername()!= null && user.getPassword()!= null) {
            if (!userDAO.existsByUsername(user.getUsername())) {
                System.out.println(user.toString());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userDAO.save(user);
                responseURL = new ResponseURL("User has been saved");
            } else {
                responseURL = new ResponseURL("User with such login already exists!!");
            }

        }else {
            responseURL = new ResponseURL("Set both fields: login and password!");
        }
        return responseURL;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findOneById(Integer id) {
        return null;
    }














    // beacause  UserService extends UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }
}

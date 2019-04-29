package oktenweb.services.impl;

import oktenweb.dao.UserDAO;
import oktenweb.models.*;
import oktenweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    public UserDetailsManager userDetailsManager;

    @Override
    public ResponseTransfer save(User user) {

        if (userDAO.existsByUsername(user.getUsername())) {
            return new ResponseTransfer("User with such login already exists!!");
        } else if(userDAO.existsByEmail(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return new ResponseTransfer("User has been saved successfully.");
        }
    }

    public ResponseTransfer changePassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return new ResponseTransfer("Password was changed successfully.");
    }

    public ResponseTransfer update(User user) {
        User userBeforeUpdate = userDAO.findOne(user.getId());
        List<String> emails = new ArrayList<>();
        List<User> users = userDAO.findAll();
        for (User user1 : users) {
            emails.add(user1.getEmail());
        }
        emails.remove(userBeforeUpdate.getEmail());
         if(emails.contains(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {

            userDAO.save(user);
            return new ResponseTransfer("User has been updated successfully.");
        }
    }

    public ResponseTransfer checkPassword(int id, String password){

        User user = userDAO.findOne(id);
        if(passwordEncoder.matches(password, user.getPassword())){
            return new ResponseTransfer("PASSWORD MATCHES");
        }else {
            return new ResponseTransfer("PASSWORD DOES NOT MATCH");
        }
    }

    @Override
    public ResponseTransfer deleteById(int id){

        String path =
                "D:\\FotoSpringRestaurantBackEnd2Rest"+ File.separator;

        User user = userDAO.findOne(id);

        if(user.getClass().equals(Client.class)){
            userDAO.delete(id);
            return new ResponseTransfer("User was deleted successfully");
        }else   {

            Restaurant restaurant = (Restaurant) user;

            List<Avatar> avatars = restaurant.getAvatars();

            for (Avatar avatar : avatars) {

                Path pathToFile =
                        FileSystems.getDefault().getPath(path + avatar.getImage());
                try {
                    Files.delete(pathToFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseTransfer("Image was not deleted");
                }
            }
            userDAO.delete(id);
            return new ResponseTransfer("User was deleted successfully");
        }

    }


    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findOneById(Integer id) {
        return userDAO.findOne(id);
    }

//    @Override
//    public User findByEmail(String email) {
//        return userDAO.findByEmail(email);
//    }
    public List<User> getLogins(){
        List<User> users = userDAO.findAll();
        List<User> logins = new ArrayList<>();
        for (User u: users) {
            User user = new User();
            user.setUsername(u.getUsername());
            user.setEmail(u.getEmail());
            logins.add(user);
        }
        return logins;
    }

    // beacause  UserService extends UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }
}

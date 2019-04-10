package oktenweb.services.impl;

import oktenweb.dao.UserDAO;
import oktenweb.models.*;
import oktenweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String save(User user) {
        String response = "";
        if (user.getUsername()!= null && user.getPassword()!= null) {
            if(!userDAO.existsByEmail(user.getEmail())) {
                if (!userDAO.existsByUsername(user.getUsername())) {
                    System.out.println(user.toString());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userDAO.save(user);
                    response = "User has been saved";
                } else {
                    response = "User with such login already exists!!";
                }
            }else {
                response = "Field email is not unique!";
            }
        }else {
            response = "Set both fields: login and password!";
        }
        return response;
    }

    @Override
    public String deleteById(int id){

        String path =
                "D:\\FotoSpringRestaurantBackEnd2Rest"+ File.separator;

        User user = userDAO.findOne(id);

        if(user.getClass().equals(Client.class)){
            userDAO.delete(id);
            return "User deleted";
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
                    return "Image was not deleted";
                }
            }
            userDAO.delete(id);
            return "User deleted";
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


    // beacause  UserService extends UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }
}

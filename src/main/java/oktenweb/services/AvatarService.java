package oktenweb.services;

import oktenweb.dao.AvatarDAO;
import oktenweb.models.Avatar;
import oktenweb.models.Restaurant;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class AvatarService {

    @Autowired
    AvatarDAO avatarDAO;
    @Autowired
    UserServiceImpl userServiceImpl;

    public List<Avatar> findByRestaurantEmail(Restaurant restaurant){
        return avatarDAO.findByRestaurantEmail(restaurant.getEmail());
    }

    private String path =  "D:\\FotoSpringRestaurantBackEnd2Rest"+ File.separator;

    public String saveAvatar
            (int restaurantId, MultipartFile image){


        try {
            image.transferTo(new File(path + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return  "Failed to add an image";
        }
        Avatar avatar = new Avatar();
        Restaurant restaurant = (Restaurant) userServiceImpl.findOneById(restaurantId);
        avatar.setRestaurant(restaurant);
        avatar.setImage(image.getOriginalFilename());
        avatarDAO.save(avatar);

        return "Image saved";
    }

    public String deleteAvatar(int id){
        Avatar avatar = avatarDAO.findOne(id);

        Path pathToFile = FileSystems.getDefault().getPath(path + avatar.getImage());
        try {
            Files.delete(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "Image was not deleted";
        }

        Restaurant restaurant = avatar.getRestaurant();
        List<Avatar> avatars = restaurant.getAvatars();
        avatars.remove(avatar);
        restaurant.setAvatars(avatars);
        avatarDAO.delete(avatar);

        return "Image was deleted";
    }

}

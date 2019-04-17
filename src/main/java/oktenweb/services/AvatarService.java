package oktenweb.services;

import oktenweb.dao.AvatarDAO;
import oktenweb.models.Avatar;
import oktenweb.models.ResponseTransfer;
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

    public ResponseTransfer saveAvatar
            (int restaurantId, MultipartFile image){


        try {
            image.transferTo(new File(path + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Failed to add an image");
        }
        Avatar avatar = new Avatar();
        Restaurant restaurant = (Restaurant) userServiceImpl.findOneById(restaurantId);
        avatar.setRestaurant(restaurant);
        avatar.setImage(image.getOriginalFilename());
        avatarDAO.save(avatar);
        return new ResponseTransfer("Image saved");
    }

    public ResponseTransfer deleteAvatar(int id){
        Avatar avatar = avatarDAO.findOne(id);

        Path pathToFile = FileSystems.getDefault().getPath(path + avatar.getImage());
        try {
            Files.delete(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Image was not deleted");
        }

        Restaurant restaurant = avatar.getRestaurant();
        List<Avatar> avatars = restaurant.getAvatars();
        avatars.remove(avatar);
        restaurant.setAvatars(avatars);
        avatarDAO.delete(avatar);
        return new ResponseTransfer("Image was deleted");
    }

}

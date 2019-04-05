package oktenweb.services;

import oktenweb.dao.AvatarDAO;
import oktenweb.models.Avatar;
import oktenweb.models.ResponseURL;
import oktenweb.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class AvatarService {

    @Autowired
    AvatarDAO avatarDAO;

    public List<Avatar> findByRestaurantName(Restaurant restaurant){
        return avatarDAO.findByRestaurantName(restaurant.getName());
    }

    public ResponseURL save(Restaurant restaurant, MultipartFile image){

        ResponseURL responseURL;
        String path =  "D:\\FotoSpringRestaurantBackEnd2Rest"+ File.separator
                +image.getOriginalFilename();
        try {
            image.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            responseURL = new ResponseURL("Failed to add an image");
        }
        Avatar avatar = new Avatar();
        avatar.setRestaurant(restaurant);
        avatar.setImage(image.getOriginalFilename());
        avatarDAO.save(avatar);
        responseURL = new ResponseURL("Image saved");
        return responseURL;
    }


}

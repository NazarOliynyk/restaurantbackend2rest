package oktenweb.services;

import oktenweb.dao.MenuSectionDAO;
import oktenweb.models.MenuSection;
import oktenweb.models.ResponseURL;
import oktenweb.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuSectionService {
    @Autowired
    MenuSectionDAO menuSectionDAO;

    public List<MenuSection> findAllByRestaurantEmail(Restaurant restaurant){
        return menuSectionDAO.findByRestaurantEmail(restaurant.getEmail());
    }

    public ResponseURL save(Restaurant restaurant, MenuSection menuSection){

        String response = "";
        List<MenuSection> menuSections = restaurant.getMenuSections();
        for (MenuSection ms : menuSections) {
            if(ms.getName().equals(menuSection.getName())){
                response = "Such menu section already exists!";
                break;
            }else {
                menuSection.setRestaurant(restaurant);
                menuSectionDAO.save(menuSection);
                response = "Menu section saved succesfully";
            }

        }

        return new ResponseURL(response);
    }
}

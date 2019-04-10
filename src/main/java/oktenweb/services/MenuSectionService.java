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

    public String saveMenuSection( MenuSection menuSection){

//        String response = "";
//        List<MenuSection> menuSections = restaurant.getMenuSections();
//        for (MenuSection ms : menuSections) {
//            if(ms.getName().equals(menuSection.getName())){
//                response = "Such menu section already exists!";
//                break;
//            }else {
//                menuSection.setRestaurant(restaurant);
//
//                response = "Menu section saved succesfully";
//            }
//        }
        menuSectionDAO.save(menuSection);
        return "Menu section saved succesfully";
    }

    public String deleteMenuSection(MenuSection menuSection){
        Restaurant restaurant = menuSection.getRestaurant();
        List<MenuSection> menuSections = restaurant.getMenuSections();
        menuSections.remove(menuSection);
        restaurant.setMenuSections(menuSections);
         menuSectionDAO.delete(menuSection);
        return "Menu Section was deleted";
    }
}

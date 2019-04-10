package oktenweb.services;

import oktenweb.dao.MenuSectionDAO;
import oktenweb.models.MenuSection;
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

        menuSectionDAO.save(menuSection);
        return "Menu section saved succesfully";
    }

    public String deleteMenuSection(int id){
        MenuSection menuSection = menuSectionDAO.findOne(id);
        Restaurant restaurant = menuSection.getRestaurant();
        List<MenuSection> menuSections = restaurant.getMenuSections();
        menuSections.remove(menuSection);
        restaurant.setMenuSections(menuSections);
         menuSectionDAO.delete(menuSection);
        return "Menu Section was deleted";
    }
}

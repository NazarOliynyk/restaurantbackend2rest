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

    public List<MenuSection> findAllByRestaurantName(Restaurant restaurant){
        return menuSectionDAO.findByRestaurantName(restaurant.getName());
    }

    public List<MenuSection> save(Restaurant restaurant, String name){

        MenuSection menuSection = new MenuSection();
        menuSection.setName(name);
        menuSection.setRestaurant(restaurant);
        menuSectionDAO.save(menuSection);

        return restaurant.getMenuSections();
    }
}

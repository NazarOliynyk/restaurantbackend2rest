package oktenweb.services;

import oktenweb.dao.MenuSectionDAO;
import oktenweb.models.MenuSection;
import oktenweb.models.ResponseTransfer;
import oktenweb.models.Restaurant;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuSectionService {
    @Autowired
    MenuSectionDAO menuSectionDAO;
    @Autowired
    UserServiceImpl userServiceImpl;

    public List<MenuSection> findAllByRestaurantId(int id){
        return menuSectionDAO.findByRestaurantId(id);
    }

    public ResponseTransfer saveMenuSection(MenuSection menuSection){

        menuSectionDAO.save(menuSection);
        return new ResponseTransfer("Menu section saved successfully");
    }

    public ResponseTransfer deleteMenuSection(int id){
        MenuSection menuSection = menuSectionDAO.findOne(id);
        Restaurant restaurant = menuSection.getRestaurant();
        List<MenuSection> menuSections = restaurant.getMenuSections();
        menuSections.remove(menuSection);
        restaurant.setMenuSections(menuSections);
        //userServiceImpl.save(restaurant);
         menuSectionDAO.delete(menuSection);
        return new ResponseTransfer("Menu Section was deleted");
    }
}

package oktenweb.dao;


import oktenweb.models.MenuSection;
import oktenweb.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuSectionDAO extends JpaRepository<MenuSection, Integer>{

    List<MenuSection> findByRestaurantId(int id);
    MenuSection findByNameAndRestaurant(String name, Restaurant restaurant);

}

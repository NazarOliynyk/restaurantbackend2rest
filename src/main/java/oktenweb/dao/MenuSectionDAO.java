package oktenweb.dao;


import oktenweb.models.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuSectionDAO extends JpaRepository<MenuSection, Integer>{

    List<MenuSection> findByRestaurantEmail(String restaurantEmail);
}

package oktenweb.dao;


import oktenweb.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDAO extends JpaRepository<Meal, Integer> {

    List<Meal> findByRestaurantEmail(String restaurantEmail);
    List<Meal> findByMenuSectionName(String menuSectionName);
}

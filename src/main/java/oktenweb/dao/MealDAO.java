package oktenweb.dao;


import oktenweb.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDAO extends JpaRepository<Meal, Integer> {

    List<Meal> findByRestaurantName(String restaurantName);
    List<Meal> findByMenuSectionName(String restaurantName);
}

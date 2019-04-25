package oktenweb.dao;

import oktenweb.models.OrderMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMealDAO extends JpaRepository<OrderMeal, Integer>{
//    List<OrderMeal> findByRestaurantEmail(String restaurantEmail);
//    List<OrderMeal> findByClientEmail(String clientEmail);
    List<OrderMeal> findAllByClientId(int id);
    List<OrderMeal> findAllByRestaurantId(int id);
}

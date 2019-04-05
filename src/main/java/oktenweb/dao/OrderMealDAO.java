package oktenweb.dao;


import oktenweb.models.OrderMeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMealDAO extends JpaRepository<OrderMeal, Integer>{
}

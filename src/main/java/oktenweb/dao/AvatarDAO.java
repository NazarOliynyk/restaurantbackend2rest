package oktenweb.dao;

import oktenweb.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AvatarDAO extends JpaRepository<Avatar, Integer>{

    List<Avatar> findByRestaurantEmail(String restaurantEmail);
}

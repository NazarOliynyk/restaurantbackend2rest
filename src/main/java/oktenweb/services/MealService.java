package oktenweb.services;

import oktenweb.dao.MealDAO;
import oktenweb.models.Meal;
import oktenweb.models.MenuSection;
import oktenweb.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    @Autowired
    MealDAO mealDAO;

    public List<Meal> findAllByRestaurantName(Restaurant restaurant){
        return mealDAO.findByRestaurantName(restaurant.getName());
    }

    public List<Meal> findByMenuSectionName(Restaurant restaurant){
        return mealDAO.findByMenuSectionName(restaurant.getName());
    }

    public List<Meal> save(Restaurant restaurant, MenuSection menuSection, Meal meal){

        meal.setRestaurant(restaurant);
        meal.setMenuSection(menuSection);
        mealDAO.save(meal);
        return restaurant.getMeals();
    }
}

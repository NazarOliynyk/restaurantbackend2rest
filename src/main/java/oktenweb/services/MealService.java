package oktenweb.services;

import oktenweb.dao.MealDAO;
import oktenweb.models.Meal;
import oktenweb.models.MenuSection;
import oktenweb.models.ResponseURL;
import oktenweb.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    @Autowired
    MealDAO mealDAO;

    public List<Meal> findAllByRestaurantEmail(Restaurant restaurant){
        return mealDAO.findByRestaurantEmail(restaurant.getEmail());
    }

    public List<Meal> findAllByMenuSectionName(MenuSection menuSection){
        return mealDAO.findByMenuSectionName(menuSection.getName());
    }

    public ResponseURL save(Restaurant restaurant, MenuSection menuSection, Meal meal){

        String response = "";
        List<Meal> meals = restaurant.getMeals();
        for (Meal m : meals) {
            if(m.getName().equals(meal.getName())){
                response = "Such meal already exists!";
                break;
            }else {
                meal.setRestaurant(restaurant);
                meal.setMenuSection(menuSection);
                mealDAO.save(meal);
                response = "Meal saved successfully!";
            }
        }

        return new ResponseURL(response);
    }
}

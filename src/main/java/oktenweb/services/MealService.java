package oktenweb.services;

import oktenweb.dao.MealDAO;
import oktenweb.models.*;
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

    public String saveMeal( Meal meal){
//        String response = "";
//        Restaurant restaurant = meal.getRestaurant();
//        List<Meal> meals = restaurant.getMeals();
//        for (Meal m : meals) {
//            if(m.getName().equals(meal.getName())){
//                response = "Such meal already exists!";
//                break;
//            }else {
//                meal.setRestaurant(restaurant);
//                meal.setMenuSection(menuSection);
//
//                response = "Meal saved successfully!";
//            }
//        }
        mealDAO.save(meal);
        return "Meal saved successfully!";
    }

    public String deleteMeal(Meal meal){
        Restaurant restaurant = meal.getRestaurant();
        List<Meal> mealsOfRestaurant = restaurant.getMeals();
        mealsOfRestaurant.remove(meal);
        restaurant.setMeals(mealsOfRestaurant);

        MenuSection menuSection = meal.getMenuSection();
        List<Meal> mealsOfMenuSection = menuSection.getMeals();
        mealsOfMenuSection.remove(meal);
        menuSection.setMeals(mealsOfMenuSection);

        List<OrderMeal> orders = meal.getOrders();
        for (OrderMeal order : orders) {
            List<Meal> mealsOfOrder = order.getMeals();
            mealsOfOrder.remove(meal);
            order.setMeals(mealsOfOrder);
        }
        mealDAO.delete(meal);
        return "Meal was deleted";
    }

}

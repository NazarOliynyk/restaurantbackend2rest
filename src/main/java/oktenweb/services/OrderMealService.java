package oktenweb.services;

import oktenweb.dao.OrderMealDAO;
import oktenweb.models.Client;
import oktenweb.models.Meal;
import oktenweb.models.OrderMeal;
import oktenweb.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMealService {

    @Autowired
    OrderMealDAO orderMealDAO;

    public List<OrderMeal> findAllByRestaurantName(Restaurant restaurant){
        return orderMealDAO.findByRestaurantEmail(restaurant.getEmail());
    }
    public List<OrderMeal> findAllByClientEmail(Client client){
        return orderMealDAO.findByClientEmail(client.getEmail());
    }

    public List<OrderMeal> save(Restaurant restaurant, Client client, Meal meal){
       OrderMeal orderMeal = new OrderMeal();
        orderMeal.setRestaurant(restaurant);
        orderMeal.setClient(client);
        List<Meal> meals = orderMeal.getMeals();
        meals.add(meal);
        orderMealDAO.save(orderMeal);
        return client.getOrders();
    }
}

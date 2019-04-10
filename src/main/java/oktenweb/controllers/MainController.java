package oktenweb.controllers;

import oktenweb.models.*;
import oktenweb.services.MealService;
import oktenweb.services.MenuSectionService;
import oktenweb.services.OrderMealService;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

@RestController
public class MainController {

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    MenuSectionService menuSectionService;
    @Autowired
    MealService mealService;
    @Autowired
    OrderMealService orderMealService;

    @CrossOrigin(origins = "*")
    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User user){

        return userServiceImpl.save(user);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {

        return userServiceImpl.deleteById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRestaurants")
    public List<Restaurant> getRestaurants(){
        List<User> users = userServiceImpl.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (User user: users) {
            if(user.getClass().equals(Restaurant.class)){
                restaurants.add((Restaurant) user);
            }
        }
        return restaurants;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getClients")
    public List<Client> getClients(){
        List<User> users = userServiceImpl.findAll();
        List<Client> clients = new ArrayList<>();
        for (User user: users) {
            if(user.getClass().equals(Client.class)){
                clients.add((Client) user);
            }
        }
        return clients;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getMenuSections")
    public List<MenuSection> getMenuSections
            (@RequestBody Restaurant restaurant){
        return menuSectionService.findAllByRestaurantEmail(restaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getMeals")
    public List<Meal> getMeals
            (@RequestBody Restaurant restaurant){
        return mealService.findAllByRestaurantEmail(restaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getClientOrders")
    public List<OrderMeal> getClientOrders
            (@RequestBody Client client){
        return orderMealService.findAllByClientEmail(client);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getRestaurantOrders")
    public List<OrderMeal> getRestaurantOrders
            (@RequestBody Restaurant restaurant){
        return orderMealService.findAllByRestaurantEmail(restaurant);
    }
}

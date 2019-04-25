package oktenweb.controllers;

import lombok.AllArgsConstructor;
import oktenweb.dao.UserDAO;
import oktenweb.models.*;
import oktenweb.services.AvatarService;
import oktenweb.services.MealService;
import oktenweb.services.MenuSectionService;
import oktenweb.services.OrderMealService;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
    @Autowired
    AvatarService avatarService;

    @CrossOrigin(origins = "*")
    @PostMapping("/saveRestaurant")
    public ResponseTransfer saveRestaurant(@RequestBody Restaurant restaurant){
        ResponseTransfer response = userServiceImpl.save(restaurant);
        System.out.println("Controller: "+response.getText());
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveClient")
    public ResponseTransfer saveClient(@RequestBody Client client){
        ResponseTransfer response = userServiceImpl.save(client);
        System.out.println("Controller: "+response.getText());
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateRestaurant")
    public ResponseTransfer updateRestaurant(@RequestBody Restaurant restaurant){

        return userServiceImpl.update(restaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateClient")
    public ResponseTransfer updateClient(@RequestBody Client client){

        return userServiceImpl.update(client);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/checkPassword/{id}")
    public ResponseTransfer checkPassword(
            @PathVariable("id") int id,
            @RequestBody String password){

        return userServiceImpl.checkPassword(id, password);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/findRestaurant/{id}")
    public Restaurant findRestaurant(@PathVariable("id") int id){
        System.out.println("id: "+id);
        return (Restaurant) userServiceImpl.findOneById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/findClient/{id}")
    public Client findClient(@PathVariable("id") int id){

        return (Client) userServiceImpl.findOneById(id);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseTransfer deleteUser(@PathVariable("id") int id) {

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
    @GetMapping("/getFiles/{id}")
    public List<File>  getFiles (@PathVariable("id") int id){
        return avatarService.findFilesByRestaurantId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAvatars/{id}")
    public List<Avatar>  getAvatars (@PathVariable("id") int id){
        return avatarService.findByRestaurantId(id);
    }

    // the following method gives encoded base64 files
    @CrossOrigin(origins = "*")
    @GetMapping("/getImages/{id}")
    public Map<String, String>  getImages
            (@PathVariable("id") int id) throws IOException {
        System.out.println("id: "+id);
        Map<String, String> jsonMap = new HashMap<>();
        List<File> files = avatarService.findFilesByRestaurantId(id);
        for (File file : files) {
            String encodeImage =
                    Base64.getEncoder().withoutPadding().
                            encodeToString(Files.readAllBytes(file.toPath()));
            jsonMap.put("content", encodeImage);
        }
        return jsonMap;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getMenuSections/{id}")
    public List<MenuSection>  getMenuSections
            (@PathVariable("id") int id){
        System.out.println("id: "+id);
        return menuSectionService.findAllByRestaurantId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getMeals/{id}")
    public List<Meal> getMeals
            (@PathVariable("id") int id){
        System.out.println("/getMeals/{id}: "+id);
        return mealService.findAllByRestaurantId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getClientOrders/{id}")
    public List<OrderMeal> getClientOrders
            (@PathVariable("id") int id){
        return orderMealService.findAllByClientId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRestaurantOrders/{id}")
    public List<OrderMeal> getRestaurantOrders
            (@PathVariable("id") int id){
        return orderMealService.findAllByRestaurantId(id);
    }
}

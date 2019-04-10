package oktenweb.controllers;


import oktenweb.models.*;
import oktenweb.services.AvatarService;
import oktenweb.services.MealService;
import oktenweb.services.MenuSectionService;
import oktenweb.services.OrderMealService;
import oktenweb.services.impl.MailServiceImpl;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RestaurantController {

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    MenuSectionService menuSectionService;
    @Autowired
    MealService mealService;
    @Autowired
    AvatarService avatarService;
    @Autowired
    OrderMealService orderMealService;
    @Autowired
    MailServiceImpl mailServiceImpl;

    private String orderAccepted = "<div>\n" +
            "    <a href=\"http://localhost:4200\" target=\"_blank\"> Your order is in process now </a>\n" +
            "</div>";

//    @PostMapping("/saveRestaurant")
//    public String saveRestaurant(@RequestBody Restaurant restaurant) {
//
//        return userServiceImpl.save(restaurant);
//    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveMenuSection")
    public String saveMenuSection( @RequestBody MenuSection menuSection) {

        return menuSectionService.saveMenuSection(menuSection);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteMenuSection/{id}")
    public String deleteMenuSection(@PathVariable int id){

        return menuSectionService.deleteMenuSection(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveMeal")
    public String saveMeal( @RequestBody Meal meal) {

        return mealService.saveMeal(meal);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteMeal/{id}")
    public String deleteMeal(@PathVariable int id){

        return mealService.deleteMeal(id);
    }


    @CrossOrigin(origins = "*")
    //@PostMapping("/saveAvatar- {xxx}")
    @PostMapping("/saveAvatar/{xxx}")
    public String saveAvatar(@PathVariable("xxx") int id,
                             @RequestBody MultipartFile image){

        return avatarService.saveAvatar(id, image);
    }


    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteAvatar/{id}")
    public String deleteAvatar(@PathVariable int id) {

        return avatarService.deleteAvatar(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/acceptOrderToKitchen")
    public String acceptOrderToKitchen(@RequestBody OrderMeal orderMeal){

        String responseFromMailSender =
                mailServiceImpl.send(orderMeal.getClient().getEmail(), orderAccepted);
        if(responseFromMailSender.equals("Message was sent")){
            orderMeal.setOrderStatus(OrderStatus.IN_PROCESS);
            return orderMealService.saveOrder(orderMeal)+" and status changed";
        }else {
            return  responseFromMailSender;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cancelOrderByRestaurant/{id}")
    public String cancelOrderByRestaurant(@PathVariable("id") int id,
                                          @RequestBody String reasonOfCancelation){

        return orderMealService.cancelOrderByRestaurant(id, reasonOfCancelation);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteOrderByRestaurant/{id]")
    public String deleteOrderByRestaurant(@PathVariable("id") int id){

        return orderMealService.deleteOrderByRestaurant(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/negativeFromRestaurant/{id}")
    public String negativeFromRestaurant(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromRestaurant){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.negativeFromRestaurant(orderMeal, descriptionFromRestaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/positiveFromRestaurant/{id}")
    public String positiveFromRestaurant(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromRestaurant){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.positiveFromRestaurant(orderMeal, descriptionFromRestaurant);
    }

}

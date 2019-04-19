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


    @CrossOrigin(origins = "*")
    @PostMapping("/saveMenuSection/{id}")
    public ResponseTransfer saveMenuSection( @PathVariable("id") int id,
                                            @RequestBody MenuSection menuSection) {
        Restaurant restaurant = (Restaurant) userServiceImpl.findOneById(id);
        menuSection.setRestaurant(restaurant);
        return menuSectionService.saveMenuSection(menuSection);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteMenuSection/{id}")
    public ResponseTransfer deleteMenuSection(@PathVariable int id){

        return menuSectionService.deleteMenuSection(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveMeal")
    public ResponseTransfer saveMeal( @RequestBody Meal meal) {

        return mealService.saveMeal(meal);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteMeal/{id}")
    public ResponseTransfer deleteMeal(@PathVariable int id){

        return mealService.deleteMeal(id);
    }


    @CrossOrigin(origins = "*")
    //@PostMapping("/saveAvatar- {xxx}")
    @PostMapping("/saveAvatar/{xxx}")
    public ResponseTransfer saveAvatar(@PathVariable("xxx") int id,
                             @RequestParam("file") MultipartFile image){
        System.out.println("id: "+id);
        System.out.println("OriginalFilename: "+image.getOriginalFilename());

         return avatarService.saveAvatar(id, image);
    }


    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteAvatar/{id}")
    public ResponseTransfer deleteAvatar(@PathVariable int id) {

        return avatarService.deleteAvatar(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/acceptOrderToKitchen")
    public ResponseTransfer acceptOrderToKitchen(@RequestBody OrderMeal orderMeal){

        String responseFromMailSender =
                mailServiceImpl.send(orderMeal.getClient().getEmail(), orderAccepted);
        if(responseFromMailSender.equals("Message was sent")){
            orderMeal.setOrderStatus(OrderStatus.IN_PROCESS);

            return new ResponseTransfer
                    (orderMealService.saveOrder(orderMeal).toString()+" and status changed");
        }else {
            return new ResponseTransfer(responseFromMailSender);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cancelOrderByRestaurant/{id}")
    public ResponseTransfer cancelOrderByRestaurant(@PathVariable("id") int id,
                                          @RequestBody String reasonOfCancelation){

        return orderMealService.cancelOrderByRestaurant(id, reasonOfCancelation);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteOrderByRestaurant/{id]")
    public ResponseTransfer deleteOrderByRestaurant(@PathVariable("id") int id){

        return orderMealService.deleteOrderByRestaurant(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/negativeFromRestaurant/{id}")
    public ResponseTransfer negativeFromRestaurant(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromRestaurant){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.negativeFromRestaurant(orderMeal, descriptionFromRestaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/positiveFromRestaurant/{id}")
    public ResponseTransfer positiveFromRestaurant(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromRestaurant){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.positiveFromRestaurant(orderMeal, descriptionFromRestaurant);
    }

}

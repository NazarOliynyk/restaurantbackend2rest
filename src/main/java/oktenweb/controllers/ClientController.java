package oktenweb.controllers;

import oktenweb.models.*;
import oktenweb.services.OrderMealService;
import oktenweb.services.impl.MailServiceImpl;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    OrderMealService orderMealService;
    @Autowired
    MailServiceImpl mailServiceImpl;       

    private String newOrder = "<div>\n" +
            "    <a href=\"http://localhost:4200\" target=\"_blank\"> You have just got a new order </a>\n" +
            "</div>";

//    @PostMapping("/saveClient")
//    public String saveClient(@RequestBody Client client) {
//
//        return userServiceImpl.save(client);
//    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveOrder")
    public String saveOrder(@RequestBody OrderMeal orderMeal){
        String responseFromMailSender =
                mailServiceImpl.send(orderMeal.getRestaurant().getEmail(), newOrder);
        if(responseFromMailSender.equals("Message was sent")){
            orderMeal.setResponseFromRestaurant(ResponseType.NEUTRAL);
            orderMeal.setResponseFromClient(ResponseType.NEUTRAL);
            orderMeal.setOrderStatus(OrderStatus.JUST_ORDERED);
            return orderMealService.saveOrder(orderMeal);
        }else {
            return responseFromMailSender;
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable int id){

        return orderMealService.deleteOrderByClient(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cancelOrderByClient/{id}")
    public String cancelOrderByClient(@PathVariable("id") int id,
                                      @RequestBody String reasonOfCancelation){

        return orderMealService.cancelOrderByClient(id, reasonOfCancelation);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/confirmOrderServed")
    public String confirmOrderServed(OrderMeal orderMeal){

        return orderMealService.confirmOrderServed(orderMeal);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/negativeFromClient/{id}")
    public String negativeFromClient(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.negativeFromClient(orderMeal, descriptionFromClient);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/positiveFromClient/{id}")
    public String positiveFromClient(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.positiveFromClient(orderMeal, descriptionFromClient);
    }
}

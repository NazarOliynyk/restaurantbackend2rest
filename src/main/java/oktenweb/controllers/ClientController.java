package oktenweb.controllers;

import oktenweb.models.*;
import oktenweb.services.OrderMealService;
import oktenweb.services.impl.MailServiceImpl;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @CrossOrigin(origins = "*")
    @PostMapping("/saveOrder")
    public ResponseTransfer saveOrder(@RequestBody OrderMeal orderMeal){
        String responseFromMailSender =
                mailServiceImpl.send(orderMeal.getRestaurant().getEmail(), newOrder);
        if(responseFromMailSender.equals("Message was sent")){
            orderMeal.setResponseFromRestaurant(TypeOfResponse.NEUTRAL);
            orderMeal.setResponseFromClient(TypeOfResponse.NEUTRAL);
            orderMeal.setOrderStatus(OrderStatus.JUST_ORDERED);
            return orderMealService.saveOrder(orderMeal);
        }else {
            return new ResponseTransfer(responseFromMailSender);
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteOrder/{id}")
    public ResponseTransfer deleteOrder(@PathVariable int id){

        return orderMealService.deleteOrderByClient(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cancelOrderByClient/{id}")
    public ResponseTransfer cancelOrderByClient(@PathVariable("id") int id,
                                      @RequestBody String reasonOfCancelation){

        return orderMealService.cancelOrderByClient(id, reasonOfCancelation);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/confirmOrderServed")
    public ResponseTransfer confirmOrderServed(OrderMeal orderMeal){

        return orderMealService.confirmOrderServed(orderMeal);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/negativeFromClient/{id}")
    public ResponseTransfer negativeFromClient(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.negativeFromClient(orderMeal, descriptionFromClient);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/positiveFromClient/{id}")
    public ResponseTransfer positiveFromClient(@PathVariable("id") int id,
                                     @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.positiveFromClient(orderMeal, descriptionFromClient);
    }
}

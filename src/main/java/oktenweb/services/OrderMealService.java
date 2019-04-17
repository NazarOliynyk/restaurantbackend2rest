package oktenweb.services;

import oktenweb.dao.OrderMealDAO;
import oktenweb.dao.UserDAO;
import oktenweb.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMealService {

    @Autowired
    OrderMealDAO orderMealDAO;
    @Autowired
    UserDAO userDAO;

    public List<OrderMeal> findAllByRestaurantEmail(Restaurant restaurant){
        return orderMealDAO.findByRestaurantEmail(restaurant.getEmail());
    }
    public List<OrderMeal> findAllByClientEmail(Client client){
        return orderMealDAO.findByClientEmail(client.getEmail());
    }

    public ResponseTransfer saveOrder(OrderMeal orderMeal){

        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Order was saved successfully");
    }

    public OrderMeal findById(int id){
        return orderMealDAO.findOne(id);
    }

    // this method is to reduce code in both: deleteOrderByClient and deleteOrderByRestaurant
    private void deleteOrderFromEverywhere(OrderMeal orderMeal){
        Restaurant restaurant = orderMeal.getRestaurant();
        List<OrderMeal> ordersOfRestaurant = restaurant.getOrders();
        ordersOfRestaurant.remove(orderMeal);
        restaurant.setOrders(ordersOfRestaurant);

        Client client = orderMeal.getClient();
        List<OrderMeal> ordersOfClient = client.getOrders();
        ordersOfClient.remove(orderMeal);
        client.setOrders(ordersOfClient);

        List<Meal> meals = orderMeal.getMeals();
        for (Meal meal : meals) {
            List<OrderMeal> ordersOfMeal = meal.getOrders();
            ordersOfMeal.remove(orderMeal);
            meal.setOrders(ordersOfMeal);
        }
    }

    public ResponseTransfer deleteOrderByClient(int id){
        OrderMeal orderMeal = orderMealDAO.findOne(id);
        if(orderMeal.getOrderStatus().equals(OrderStatus.JUST_ORDERED)||
                orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return new ResponseTransfer("Order was deleted");

        }else {
            return new ResponseTransfer
                    ("Can not delete - your order is not just-ordered or canceled by the Restaurant");

        }
    }

    public ResponseTransfer deleteOrderByRestaurant(int id){
        OrderMeal orderMeal = orderMealDAO.findOne(id);
        if(orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_CLIENT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return new ResponseTransfer("Order was deleted");

        }else {
            return new ResponseTransfer
                    ("Can not delete - your order is not canceled by the Client");

        }
    }

    public ResponseTransfer cancelOrderByRestaurant(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.findOne(id);
        orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_RESTAURANT);
        orderMeal.setReasonOfCancelation(reasonOfCancelation);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Status was changed to Canceled by Restaurant");
    }

    public ResponseTransfer cancelOrderByClient(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.findOne(id);
        orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_CLIENT);
        orderMeal.setReasonOfCancelation(reasonOfCancelation);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Status was changed to Canceled by Client");
    }

    public ResponseTransfer confirmOrderServed(OrderMeal orderMeal){
        orderMeal.setOrderStatus(OrderStatus.SERVED);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Order status - Served");
    }

    public void recountClientResponses(OrderMeal order){
        Client client = order.getClient();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: client.getOrders()) {
            if(ord.getResponseFromRestaurant().equals(TypeOfResponse.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromRestaurant().equals(TypeOfResponse.POSITIVE)){
                positive.add(ord);}
        }
        client.setClientNegativeResponses(negative.size());
        client.setClientPositiveResponses(positive.size());
        userDAO.save(client);
    }

    public void recountRestaurantResponses(OrderMeal order){
        Restaurant restaurant = order.getRestaurant();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: restaurant.getOrders()) {
            if(ord.getResponseFromClient().equals(TypeOfResponse.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromClient().equals(TypeOfResponse.POSITIVE)) {
                positive.add(ord);}
        }
        restaurant.setRestaurantNegativeResponses(negative.size());
        restaurant.setRestaurantPositiveResponses(positive.size());
        userDAO.save(restaurant);
    }

    public ResponseTransfer negativeFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(TypeOfResponse.NEGATIVE);
            recountRestaurantResponses(orderMeal);
            orderMealDAO.save(orderMeal);
            return new ResponseTransfer("Response changed to negative");

        }else {
            return new ResponseTransfer("Can not change the RESPONSE to your order, change order status to SERVED");
        }
    }

    public ResponseTransfer positiveFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(TypeOfResponse.POSITIVE);
            recountRestaurantResponses(orderMeal);
            orderMealDAO.save(orderMeal);
            return new ResponseTransfer("Response changed to positive");
        }else {
            return new ResponseTransfer
                    ("Can not change the RESPONSE to your order, change order status to SERVED");
        }
    }

    public ResponseTransfer negativeFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(TypeOfResponse.NEGATIVE);
        recountClientResponses(orderMeal);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Response changed to negative");
    }

    public ResponseTransfer positiveFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(TypeOfResponse.POSITIVE);
        recountClientResponses(orderMeal);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Response changed to positive");
    }

}

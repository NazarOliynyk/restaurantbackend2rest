package oktenweb.services;

import oktenweb.dao.OrderMealDAO;
import oktenweb.dao.UserDAO;
import oktenweb.models.*;
import org.aspectj.weaver.ast.Or;
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

    public String saveOrder(OrderMeal orderMeal){

        orderMealDAO.save(orderMeal);
        return "Order was saved successfully";
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

    public String deleteOrderByClient(OrderMeal orderMeal){
        if(orderMeal.getOrderStatus().equals(OrderStatus.JUST_ORDERED)||
                orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return "Order was deleted";

        }else {
            return "Can not delete - your order is not just-ordered or canceled by the Restaurant";
        }
    }

    public String deleteOrderByRestaurant(OrderMeal orderMeal){
        if(orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_CLIENT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return "Order was deleted";

        }else {
            return "Can not delete - your order is not canceled by the Client";
        }
    }

    public String cancelOrderByRestaurant(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.findOne(id);
        orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_RESTAURANT);
        orderMeal.setReasonOfCancelation(reasonOfCancelation);
        orderMealDAO.save(orderMeal);

        return "Status was changed to Canceled by Restaurant";
    }

    public String cancelOrderByClient(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.findOne(id);
        orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_CLIENT);
        orderMeal.setReasonOfCancelation(reasonOfCancelation);
        orderMealDAO.save(orderMeal);

        return "Status was changed to Canceled by Client";
    }

    public String confirmOrderServed(OrderMeal orderMeal){
        orderMeal.setOrderStatus(OrderStatus.SERVED);
        orderMealDAO.save(orderMeal);
        return "Order status - Served";
    }

    public void recountClientResponses(OrderMeal order){
        Client client = order.getClient();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: client.getOrders()) {
            if(ord.getResponseFromRestaurant().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromRestaurant().equals(ResponseType.POSITIVE)){
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
            if(ord.getResponseFromClient().equals(ResponseType.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromClient().equals(ResponseType.POSITIVE)) {
                positive.add(ord);}
        }
        restaurant.setRestaurantNegativeResponses(negative.size());
        restaurant.setRestaurantPositiveResponses(positive.size());
        userDAO.save(restaurant);
    }

    public String negativeFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(ResponseType.NEGATIVE);
            recountRestaurantResponses(orderMeal);
            orderMealDAO.save(orderMeal);
            return "Response changed to negative";
        }else {
            return "Can not change the RESPONSE to your order, change order status to SERVED";
        }
    }

    public String positiveFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(ResponseType.POSITIVE);
            recountRestaurantResponses(orderMeal);
            orderMealDAO.save(orderMeal);
            return "Response changed to positive";
        }else {
            return "Can not change the RESPONSE to your order, change order status to SERVED";
        }
    }

    public String negativeFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(ResponseType.NEGATIVE);
        recountClientResponses(orderMeal);
        orderMealDAO.save(orderMeal);
        return "Response changed to negative";
    }

    public String positiveFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(ResponseType.POSITIVE);
        recountClientResponses(orderMeal);
        orderMealDAO.save(orderMeal);
        return "Response changed to positive";
    }

}

package oktenweb.services;


import oktenweb.dao.UserDAO;
import oktenweb.models.Client;
import oktenweb.models.ResponseURL;
import oktenweb.models.Restaurant;
import oktenweb.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SuccessURLService {
    @Autowired
    UserDAO userDAO;
    public ResponseURL getResponse(){

        ResponseURL responseURL ;
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println("auth.getName(): "+auth.getName());
        System.out.println("auth.toString(): "+auth.toString());
        System.out.println("auth.getPrincipal(): "+auth.getPrincipal());
        System.out.println("auth.getCredentials(): "+auth.getCredentials());
        System.out.println("auth.getDetails(): "+auth.getDetails());
        System.out.println("auth.getClass(): "+auth.getClass());

        User userLogged = userDAO.findByUsername(auth.getName());
        //   System.out.println("userLogged.toString(): "+userLogged.toString());
        //  System.out.println("userLogged.getClass(): "+userLogged.getClass());

        Restaurant restaurant ;
        Client client ;
        if(auth.getName().equals("admin")){
            System.out.println("IT IS ADMIN, Who Just LOGGED in!!");
            responseURL= new ResponseURL("ADMIN");
        }
        else {
            if(userLogged.getClass().equals(oktenweb.models.Restaurant.class)){
                System.out.println("USER -> RESTAURANT");
                restaurant = (Restaurant) userLogged;
                responseURL = new ResponseURL("RESTAURANT", restaurant);
            }else if (userLogged.getClass().equals(oktenweb.models.Client.class)){
                System.out.println("USER -> CLIENT");
                client = (Client) userLogged;
                responseURL = new ResponseURL("CLIENT", client);
            }else {
                System.out.println("UNDEFINED CLASS!");
                responseURL = new ResponseURL("UNDEFINED CLASS!");
            }
        }

        return responseURL;
    }



}

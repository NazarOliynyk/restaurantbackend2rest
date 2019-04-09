package oktenweb.controllers;


import oktenweb.models.Meal;
import oktenweb.models.MenuSection;
import oktenweb.models.ResponseURL;
import oktenweb.models.Restaurant;
import oktenweb.services.MealService;
import oktenweb.services.MenuSectionService;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    MenuSectionService menuSectionService;
    @Autowired
    MealService mealService;

    @PostMapping("/saveClient")
    public ResponseURL saveRestaurant(@RequestBody Restaurant restaurant) {

        return userServiceImpl.save(restaurant);
    }

    @PostMapping("/saveMenuSection")
    public ResponseURL saveRestaurant(@RequestBody Restaurant restaurant,
                                      @RequestBody MenuSection menuSection) {

        return menuSectionService.save(restaurant, menuSection);
    }

    @PostMapping("/saveMeal")
    public ResponseURL saveMeal(@RequestBody Restaurant restaurant,
                                 @RequestBody MenuSection menuSection,
                                @RequestBody Meal meal) {

        return mealService.save(restaurant, menuSection, meal);
    }

}

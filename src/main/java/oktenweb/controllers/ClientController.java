package oktenweb.controllers;

import oktenweb.models.Client;
import oktenweb.models.ResponseURL;
import oktenweb.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/saveClient")
    public ResponseURL saveClient(@RequestBody Client client) {

        return userServiceImpl.save(client);
    }

}

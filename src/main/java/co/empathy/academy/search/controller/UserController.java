package co.empathy.academy.search.controller;

import co.empathy.academy.search.models.User;
import co.empathy.academy.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    public UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/user/list")
    @ResponseBody
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.listUsers());
    }

    @RequestMapping("user/get/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @RequestMapping(value = "user/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@RequestBody String id) {
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


}

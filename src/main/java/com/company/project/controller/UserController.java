package com.company.project.controller;

import com.company.project.core.ListResult;
import com.company.project.core.PageFilter;
import com.company.project.model.User;
import com.company.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @author CodeGenerator
* @date 2018-08-08
*/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return userService.update(user).orElse(null);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getById(id).orElse(null);
    }

    @PostMapping("/page")
    public ListResult<User> pageUsers(@RequestBody PageFilter<User> filter) {
        return userService.page(filter);
    }
}

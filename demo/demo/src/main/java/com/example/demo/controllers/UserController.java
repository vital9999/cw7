package com.example.demo.controllers;

import com.example.demo.model.Customer;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private StockRepo stockRepo;
    @Autowired
    private ProducrRepo producrRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {

        User userFromDb = userRepo.findByLogin(user.getLogin());

        if (userFromDb != null) {
              model.put("message", "User exists!");
              return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
    @GetMapping("/main/user")
    public String user(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        return "user";
    }
    @PostMapping("/main/user")
    public String userChange(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("user", user);

        userRepo.save(user);
        return "main";
    }
    @GetMapping("/main/user/customer")
    public String userCust(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("user", user);

        return "customer";
    }
    @PostMapping("/main/user/customer")
    public String userCustomer(Customer customer, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        if(customerRepo.findByUser(user).isEmpty()) {
            customer.setUser(user);
            customerRepo.save(customer);
        }
        return "user";
    }
    @GetMapping("/main/user/delete")
    public String userDelete(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("user", user);
        userRepo.delete(user);
        return "redirect:/logout";
    }
}
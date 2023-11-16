package com.tumanyan.warehouse.controller;

import com.tumanyan.warehouse.entity.User;
import com.tumanyan.warehouse.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final CustomUserDetailService userService;

    @Autowired
    public HomeController(CustomUserDetailService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String regPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String regPostPage(@ModelAttribute User user, ModelAndView model) {
        if (!(userService.registrationUser(user))) {
            model.addObject("unsuccessfully", "Неудалось создать пользователя! Возможно такой польователь уже существует!");
            return "registration";
        }
        return "redirect:/profile";
    }
}

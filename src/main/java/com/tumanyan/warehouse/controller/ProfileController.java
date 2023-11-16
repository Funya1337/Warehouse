package com.tumanyan.warehouse.controller;

import com.tumanyan.warehouse.service.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    private final CustomUserDetailService userService;

    @Autowired
    public ProfileController(CustomUserDetailService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

    @PostMapping("/profile/delete")
    public String deletePost(HttpServletRequest request) {
        userService.deleteUser(request);
        return "redirect:/";
    }
}

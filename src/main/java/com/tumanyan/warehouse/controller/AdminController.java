package com.tumanyan.warehouse.controller;

import com.tumanyan.warehouse.entity.User;
import com.tumanyan.warehouse.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final CustomUserDetailService userService;

    @Autowired
    public AdminController(CustomUserDetailService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/panel")
    public String adminPage(Model model) {
        String message = (String) model.getAttribute("blocked");
        if (message != null) {
            model.addAttribute("blocked", message);
        }

        Iterable<User> users = userService.getUserList();
        model.addAttribute("users", users);
        return "admin-panel";
    }

    @PostMapping("/admin/block-user")
    public String blockUser(@RequestParam Long id, RedirectAttributes attributes) {
        attributes.addFlashAttribute("blocked", userService.blockUser(id));
        return "redirect:/admin/panel";
    }
}

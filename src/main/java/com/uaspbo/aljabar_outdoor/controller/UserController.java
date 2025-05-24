package com.uaspbo.aljabar_outdoor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    public String userHome() {
        return "user/home";
    }

    @GetMapping("/profile")
    public String userProfile() {
        return "user/profile";
    }
}

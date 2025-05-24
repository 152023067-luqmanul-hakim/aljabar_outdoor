package com.uaspbo.aljabar_outdoor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.uaspbo.aljabar_outdoor.model.User;
import com.uaspbo.aljabar_outdoor.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        // user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(User.Role.USER); 
        userRepository.save(user);
        return "redirect:/login";
    }
    

    // Mengarahkan user ke halaman sesuai role
    @GetMapping("/default")
    public String defaultAfterLogin(Authentication auth) {
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/user/home";
    }

}

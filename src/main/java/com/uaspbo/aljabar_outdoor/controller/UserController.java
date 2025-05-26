package com.uaspbo.aljabar_outdoor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import com.uaspbo.aljabar_outdoor.model.Product;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/home")
    public String userHome(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "user/home";
    }

    @GetMapping("/profile")
    public String userProfile() {
        return "user/profile";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "user/product-detail";
    }
}

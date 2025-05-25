package com.uaspbo.aljabar_outdoor.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uaspbo.aljabar_outdoor.model.Product;
import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }   

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/dataProduct")
    public String showProductList(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin/dataProduct";
    }

    @PostMapping("/dataProduct")
    public String addProduct(Product product) {
        productRepository.save(product);
        return "redirect:/admin/dataProduct";
    }
}

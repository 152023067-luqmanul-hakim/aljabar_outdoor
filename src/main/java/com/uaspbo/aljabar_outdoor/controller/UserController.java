package com.uaspbo.aljabar_outdoor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import com.uaspbo.aljabar_outdoor.repository.UserRepository;
import com.uaspbo.aljabar_outdoor.model.Product;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String userHome(Model model, @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "kategori", required = false) String kategori) {
        List<Product> products;
        Product.Kategori kategoriEnum = null;
        if (kategori != null && !kategori.isEmpty()) {
            try {
                kategoriEnum = Product.Kategori.valueOf(kategori);
            } catch (Exception e) {
                kategoriEnum = null;
            }
        }
        if (kategoriEnum != null) {
            if ("harga_terendah".equals(sort)) {
                products = productRepository.findByKategoriOrderByHargaSewaPerHariAsc(kategoriEnum);
            } else if ("harga_tertinggi".equals(sort)) {
                products = productRepository.findByKategoriOrderByHargaSewaPerHariDesc(kategoriEnum);
            } else if ("terbaru".equals(sort)) {
                products = productRepository.findByKategoriOrderByIdProdukDesc(kategoriEnum);
            } else if ("terpopuler".equals(sort)) {
                products = productRepository.findByKategori(kategoriEnum); // Ganti dengan logic populer jika ada
            } else {
                products = productRepository.findByKategori(kategoriEnum);
            }
        } else {
            if ("harga_terendah".equals(sort)) {
                products = productRepository.findAllByOrderByHargaSewaPerHariAsc();
            } else if ("harga_tertinggi".equals(sort)) {
                products = productRepository.findAllByOrderByHargaSewaPerHariDesc();
            } else if ("terbaru".equals(sort)) {
                products = productRepository.findAllByOrderByIdProdukDesc();
            } else if ("terpopuler".equals(sort)) {
                products = productRepository.findAll(); // Ganti dengan logic populer jika ada
            } else {
                products = productRepository.findAll();
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("sort", sort);
        model.addAttribute("kategori", kategori);
        return "user/home";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        com.uaspbo.aljabar_outdoor.model.User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        if (userDetails != null) {
            com.uaspbo.aljabar_outdoor.model.User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", user);
        }
        return "user/product-detail";
    }

    @PostMapping("/profile")
    public String updateProfile(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails, @ModelAttribute("user") com.uaspbo.aljabar_outdoor.model.User formUser, @org.springframework.web.bind.annotation.RequestParam(value = "confirmPassword", required = false) String confirmPassword) {
        com.uaspbo.aljabar_outdoor.model.User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user != null) {
            user.setNamaLengkap(formUser.getNamaLengkap());
            user.setAlamat(formUser.getAlamat());
            user.setNoTelepon(formUser.getNoTelepon());
            if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
                if (confirmPassword == null || !formUser.getPassword().equals(confirmPassword)) {
                    model.addAttribute("user", user);
                    model.addAttribute("error", "Password baru dan konfirmasi password tidak sama.");
                    return "user/profile";
                }
                user.setPassword(formUser.getPassword()); // tambahkan encoder jika perlu
            }
            userRepository.save(user);
            model.addAttribute("user", user);
            model.addAttribute("success", "Profil berhasil diperbarui");
        }
        return "user/profile";
    }
}

package com.uaspbo.aljabar_outdoor.controller;

import com.uaspbo.aljabar_outdoor.model.Keranjang;
import com.uaspbo.aljabar_outdoor.model.Product;
import com.uaspbo.aljabar_outdoor.model.User;
import com.uaspbo.aljabar_outdoor.repository.KeranjangRepository;
import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import com.uaspbo.aljabar_outdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/keranjang")
public class KeranjangController {

    @Autowired
    private KeranjangRepository keranjangRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showKeranjang(Model model, Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Keranjang> keranjangList = keranjangRepository.findByUserIdUser(user.getIdUser());
            model.addAttribute("keranjangList", keranjangList);
            return "user/keranjang";
        }
        return "redirect:/login?error=userNotFound";
    }

    @PostMapping("/add/{productId}")
    public String addToKeranjang(@PathVariable Integer productId, @RequestParam Integer jumlah, Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            User user = optionalUser.get();
            Product product = optionalProduct.get();

            Keranjang existing = keranjangRepository.findByUserIdUserAndProductIdProduk(user.getIdUser(), productId);
            if (existing != null) {
                existing.setJumlah(existing.getJumlah() + jumlah);
                keranjangRepository.save(existing);
            } else {
                Keranjang keranjang = new Keranjang();
                keranjang.setUser(user);
                keranjang.setProduct(product);
                keranjang.setJumlah(jumlah);
                keranjangRepository.save(keranjang);
            }
        }

        return "redirect:/user/keranjang";
    }

    @PostMapping("/remove/{id}")
    public String removeFromKeranjang(@PathVariable Integer id) {
        keranjangRepository.deleteById(id);
        return "redirect:/user/keranjang";
    }

    @PostMapping("/clear")
    public String clearKeranjang(Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Keranjang> keranjangList = keranjangRepository.findByUserIdUser(user.getIdUser());
            keranjangRepository.deleteAll(keranjangList);
        }
        return "redirect:/user/keranjang";
    }
}

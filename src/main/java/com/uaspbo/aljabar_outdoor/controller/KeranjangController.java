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
            // Hitung total harga
            java.math.BigDecimal totalHarga = java.math.BigDecimal.ZERO;
            for (Keranjang item : keranjangList) {
                if (item.getTipeAksi() == Keranjang.TipeAksi.SEWA) {
                    totalHarga = totalHarga.add(item.getProduct().getHargaSewaPerHari().multiply(java.math.BigDecimal.valueOf(item.getJumlah())));
                } else {
                    totalHarga = totalHarga.add(item.getProduct().getHargaJual().multiply(java.math.BigDecimal.valueOf(item.getJumlah())));
                }
            }
            model.addAttribute("totalHarga", totalHarga);
            return "user/keranjang";
        }
        return "redirect:/login?error=userNotFound";
    }

    @PostMapping("/add/{productId}")
    public String addToKeranjang(
        @PathVariable("productId") Integer productId,
        @RequestParam("jumlah") Integer jumlah,
        @RequestParam("actionType") String actionType,
        Principal principal,
        Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            User user = optionalUser.get();
            Product product = optionalProduct.get();

            Keranjang.TipeAksi tipeAksi;
            if ("sewa".equalsIgnoreCase(actionType)) {
                tipeAksi = Keranjang.TipeAksi.SEWA;
            } else {
                tipeAksi = Keranjang.TipeAksi.BELI;
            }

            int stokTersedia = 0;
            if (tipeAksi == Keranjang.TipeAksi.SEWA) {
                stokTersedia = product.getStokSewa();
            } else if (tipeAksi == Keranjang.TipeAksi.BELI) {
                stokTersedia = product.getStokJual();
            }
            if (jumlah <= 0 || jumlah > stokTersedia) {
                model.addAttribute("error", "Jumlah tidak valid atau stok tidak cukup.");
                return "redirect:/user/product/" + productId + "?error=stok";
            }

            Keranjang existing = keranjangRepository.findByUserIdUserAndProductIdProdukAndTipeAksi(user.getIdUser(), productId, tipeAksi);
            if (existing != null) {
                int totalJumlah = existing.getJumlah() + jumlah;
                if (totalJumlah > stokTersedia) {
                    model.addAttribute("error", "Jumlah melebihi stok tersedia.");
                    return "redirect:/user/product/" + productId + "?error=stok";
                }
                existing.setJumlah(totalJumlah);
                keranjangRepository.save(existing);
            } else {
                Keranjang keranjang = new Keranjang();
                keranjang.setUser(user);
                keranjang.setProduct(product);
                keranjang.setJumlah(jumlah);
                keranjang.setTipeAksi(tipeAksi);
                keranjangRepository.save(keranjang);
            }
        }
        return "redirect:/user/keranjang";
    }

    @PostMapping("/remove/{id}")
    public String removeFromKeranjang(@PathVariable("id") Integer id) {
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

    @PostMapping("/update/{idKeranjang}")
    public String updateJumlahKeranjang(@PathVariable("idKeranjang") Integer idKeranjang, @RequestParam("action") String action, Principal principal) {
        Optional<Keranjang> optionalKeranjang = keranjangRepository.findById(idKeranjang);
        if (optionalKeranjang.isPresent()) {
            Keranjang keranjang = optionalKeranjang.get();
            Product product = keranjang.getProduct();
            int stokTersedia = 0;
            if (keranjang.getTipeAksi() == Keranjang.TipeAksi.SEWA) {
                stokTersedia = product.getStokSewa();
            } else if (keranjang.getTipeAksi() == Keranjang.TipeAksi.BELI) {
                stokTersedia = product.getStokJual();
            }
            int jumlah = keranjang.getJumlah();
            if ("increment".equals(action) && jumlah < stokTersedia) {
                keranjang.setJumlah(jumlah + 1);
                keranjangRepository.save(keranjang);
            } else if ("decrement".equals(action) && jumlah > 1) {
                keranjang.setJumlah(jumlah - 1);
                keranjangRepository.save(keranjang);
            }
        }
        return "redirect:/user/keranjang";
    }
}

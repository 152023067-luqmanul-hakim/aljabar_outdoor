package com.uaspbo.aljabar_outdoor.controller;

import com.uaspbo.aljabar_outdoor.model.Transaksi;
import com.uaspbo.aljabar_outdoor.model.User;
import com.uaspbo.aljabar_outdoor.model.Keranjang;
import com.uaspbo.aljabar_outdoor.repository.TransaksiRepository;
import com.uaspbo.aljabar_outdoor.repository.KeranjangRepository;
import com.uaspbo.aljabar_outdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/transaksi")
public class TransaksiController {
    @Autowired
    private TransaksiRepository transaksiRepository;
    @Autowired
    private KeranjangRepository keranjangRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Keranjang> keranjangList = keranjangRepository.findByUserIdUser(user.getIdUser());
            if (keranjangList.isEmpty()) {
                model.addAttribute("error", "Keranjang kosong!");
                return "redirect:/user/keranjang";
            }
            // Buat transaksi baru
            Transaksi transaksi = new Transaksi();
            transaksi.setUser(user);
            transaksi.setTanggalTransaksi(LocalDateTime.now());
            transaksi.setStatus("PENDING");
            transaksiRepository.save(transaksi);
            // TODO: Insert ke detail transaksi (jual/peminjaman) sesuai jenis produk
            keranjangRepository.deleteAll(keranjangList);
            return "redirect:/user/transaksi/riwayat";
        }
        return "redirect:/login?error=userNotFound";
    }

    @GetMapping("/riwayat")
    public String riwayatTransaksi(Principal principal, Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Transaksi> transaksiList = transaksiRepository.findByUserIdUser(user.getIdUser());
            model.addAttribute("transaksiList", transaksiList);
            return "user/riwayat-transaksi";
        }
        return "redirect:/login?error=userNotFound";
    }
}

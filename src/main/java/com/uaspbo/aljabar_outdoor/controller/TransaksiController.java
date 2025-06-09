package com.uaspbo.aljabar_outdoor.controller;

import com.uaspbo.aljabar_outdoor.model.Transaksi;
import com.uaspbo.aljabar_outdoor.model.User;
import com.uaspbo.aljabar_outdoor.model.Keranjang;
import com.uaspbo.aljabar_outdoor.model.Product;
import com.uaspbo.aljabar_outdoor.repository.TransaksiRepository;
import com.uaspbo.aljabar_outdoor.repository.KeranjangRepository;
import com.uaspbo.aljabar_outdoor.repository.UserRepository;
import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        // Endpoint ini sudah tidak digunakan, arahkan ke keranjang
        return "redirect:/user/keranjang";
    }

    @PostMapping("/checkout-beli")
    public String checkoutBeli(Principal principal, @RequestParam("metodePembayaran") String metodePembayaranStr, Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Keranjang> keranjangList = keranjangRepository.findByUserIdUser(user.getIdUser());
            List<Keranjang> keranjangBeli = keranjangList.stream()
                    .filter(k -> k.getTipeAksi() == Keranjang.TipeAksi.BELI)
                    .toList();
            if (keranjangBeli.isEmpty()) {
                model.addAttribute("error", "Keranjang beli kosong!");
                return "redirect:/user/keranjang";
            }
            // Buat transaksi jual (parent)
            com.uaspbo.aljabar_outdoor.model.TransaksiJual transaksiJual = new com.uaspbo.aljabar_outdoor.model.TransaksiJual();
            transaksiJual.setUser(user);
            transaksiJual.setTanggalTransaksi(java.time.LocalDateTime.now());
            transaksiJual.setStatus(com.uaspbo.aljabar_outdoor.model.Transaksi.StatusTransaksi.Diproses);
            transaksiJual.setJenisTransaksi(com.uaspbo.aljabar_outdoor.model.Transaksi.JenisTransaksi.Beli);
            java.util.List<com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual> detailList = new java.util.ArrayList<>();
            for (Keranjang item : keranjangBeli) {
                com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual detail = new com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual();
                detail.setTransaksiJual(transaksiJual);
                detail.setProduct(item.getProduct());
                detail.setJumlah(item.getJumlah());
                detail.setHargaSatuan(item.getProduct().getHargaJual());
                detail.setSubtotal(item.getProduct().getHargaJual().multiply(java.math.BigDecimal.valueOf(item.getJumlah())));
                detailList.add(detail);
                // KURANGI STOK JUAL
                Product produk = item.getProduct();
                Integer stokJual = produk.getStokJual() != null ? produk.getStokJual() : 0;
                produk.setStokJual(Math.max(0, stokJual - item.getJumlah()));
                productRepository.save(produk);
            }
            // Hitung total transaksi
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            for (com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual detail : detailList) {
                total = total.add(detail.getSubtotal());
            }
            transaksiJual.setDetailList(detailList);
            transaksiJual.setTotal(total);

            // Set metode pembayaran dari request
            try {
                transaksiJual.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiJual.MetodePembayaran.valueOf(metodePembayaranStr));
            } catch (Exception e) {
                transaksiJual.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiJual.MetodePembayaran.COD);
            }
            transaksiRepository.save(transaksiJual);
            // Hapus keranjang beli
            keranjangRepository.deleteAll(keranjangBeli);
            return "redirect:/user/transaksi/riwayat";
        }
        return "redirect:/login?error=userNotFound";
    }

    @PostMapping("/checkout-sewa")
    public String checkoutSewa(Principal principal,
                               @RequestParam("tanggalMulai") String tanggalMulaiStr,
                               @RequestParam("tanggalSelesai") String tanggalSelesaiStr,
                               Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Keranjang> keranjangList = keranjangRepository.findByUserIdUser(user.getIdUser());
            List<Keranjang> keranjangSewa = keranjangList.stream()
                    .filter(k -> k.getTipeAksi() == Keranjang.TipeAksi.SEWA)
                    .toList();
            if (keranjangSewa.isEmpty()) {
                model.addAttribute("error", "Keranjang sewa kosong!");
                return "redirect:/user/keranjang";
            }
            java.time.LocalDate tanggalMulai = java.time.LocalDate.parse(tanggalMulaiStr);
            java.time.LocalDate tanggalSelesai = java.time.LocalDate.parse(tanggalSelesaiStr);
            com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman transaksiPeminjaman = new com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman();
            transaksiPeminjaman.setUser(user);
            transaksiPeminjaman.setTanggalTransaksi(java.time.LocalDateTime.now());
            transaksiPeminjaman.setStatus(com.uaspbo.aljabar_outdoor.model.Transaksi.StatusTransaksi.Diproses);
            transaksiPeminjaman.setJenisTransaksi(com.uaspbo.aljabar_outdoor.model.Transaksi.JenisTransaksi.Sewa);
            transaksiPeminjaman.setTanggalMulai(tanggalMulai);
            transaksiPeminjaman.setTanggalSelesai(tanggalSelesai);
            java.util.List<com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman> detailList = new java.util.ArrayList<>();
            for (Keranjang item : keranjangSewa) {
                com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman detail = new com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman();
                detail.setTransaksiPeminjaman(transaksiPeminjaman);
                detail.setProduct(item.getProduct());
                detail.setJumlah(item.getJumlah());
                detail.setHargaPerHari(item.getProduct().getHargaSewaPerHari());
                detail.setSubtotal(item.getProduct().getHargaSewaPerHari().multiply(java.math.BigDecimal.valueOf(item.getJumlah())));
                // KURANGI STOK SEWA
                Product produk = item.getProduct();
                Integer stokSewa = produk.getStokSewa() != null ? produk.getStokSewa() : 0;
                produk.setStokSewa(Math.max(0, stokSewa - item.getJumlah()));
                productRepository.save(produk);
                detailList.add(detail);
            }
            transaksiPeminjaman.setDetailList(detailList);
            transaksiRepository.save(transaksiPeminjaman);
            keranjangRepository.deleteAll(keranjangSewa);
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

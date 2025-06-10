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
import java.util.List;
import java.util.Objects;
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
    public String checkoutBeli(Principal principal,
                               @RequestParam("metodePembayaran") String metodePembayaranStr,
                               @RequestParam("namaLengkap") String namaLengkap,
                               @RequestParam("noTelepon") String noTelepon,
                               @RequestParam("alamat") String alamat,
                               Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Sinkronisasi data user jika ada perubahan data pengiriman
            boolean userChanged = false;
            if (!Objects.equals(user.getNamaLengkap(), namaLengkap)) {
                user.setNamaLengkap(namaLengkap);
                userChanged = true;
            }
            if (!Objects.equals(user.getNoTelepon(), noTelepon)) {
                user.setNoTelepon(noTelepon);
                userChanged = true;
            }
            if (!Objects.equals(user.getAlamat(), alamat)) {
                user.setAlamat(alamat);
                userChanged = true;
            }
            if (userChanged) {
                userRepository.save(user);
            }
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
            // Simpan data pengiriman dari form
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
                               @RequestParam("namaLengkap") String namaLengkap,
                               @RequestParam("noTelepon") String noTelepon,
                               @RequestParam("alamat") String alamat,
                               @RequestParam("metodePembayaran") String metodePembayaranStr,
                               Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Sinkronisasi data user jika ada perubahan data pengiriman
            boolean userChanged = false;
            if (!Objects.equals(user.getNamaLengkap(), namaLengkap)) {
                user.setNamaLengkap(namaLengkap);
                userChanged = true;
            }
            if (!Objects.equals(user.getNoTelepon(), noTelepon)) {
                user.setNoTelepon(noTelepon);
                userChanged = true;
            }
            if (!Objects.equals(user.getAlamat(), alamat)) {
                user.setAlamat(alamat);
                userChanged = true;
            }
            if (userChanged) {
                userRepository.save(user);
            }
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
            // Set metode pembayaran
            try {
                transaksiPeminjaman.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman.MetodePembayaran.valueOf(metodePembayaranStr));
            } catch (Exception e) {
                transaksiPeminjaman.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman.MetodePembayaran.COD);
            }
            java.util.List<com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman> detailList = new java.util.ArrayList<>();
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            long days = java.time.temporal.ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
            if (days < 1) days = 1; // Minimal 1 hari
            for (Keranjang item : keranjangSewa) {
                com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman detail = new com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman();
                detail.setTransaksiPeminjaman(transaksiPeminjaman);
                detail.setProduct(item.getProduct());
                detail.setJumlah(item.getJumlah());
                detail.setHargaPerHari(item.getProduct().getHargaSewaPerHari());
                java.math.BigDecimal subtotal = item.getProduct().getHargaSewaPerHari().multiply(java.math.BigDecimal.valueOf(item.getJumlah()));
                detail.setSubtotal(subtotal);
                total = total.add(subtotal.multiply(java.math.BigDecimal.valueOf(days)));
                // KURANGI STOK SEWA
                Product produk = item.getProduct();
                Integer stokSewa = produk.getStokSewa() != null ? produk.getStokSewa() : 0;
                produk.setStokSewa(Math.max(0, stokSewa - item.getJumlah()));
                productRepository.save(produk);
                detailList.add(detail);
            }
            transaksiPeminjaman.setDetailList(detailList);
            transaksiPeminjaman.setTotal(total);
            transaksiRepository.save(transaksiPeminjaman);
            keranjangRepository.deleteAll(keranjangSewa);
            return "redirect:/user/transaksi/riwayat";
        }
        return "redirect:/login?error=userNotFound";
    }

    @PostMapping("/checkout-product")
    public String checkoutProduct(
            Principal principal,
            @RequestParam("idProduk") Integer idProduk,
            @RequestParam("jumlah") Integer jumlah,
            @RequestParam("actionType") String actionType,
            @RequestParam("namaLengkap") String namaLengkap,
            @RequestParam("noTelepon") String noTelepon,
            @RequestParam("alamat") String alamat,
            @RequestParam(value = "metodePembayaran") String metodePembayaranStr,
            @RequestParam(value = "tanggalMulai", required = false) String tanggalMulaiStr,
            @RequestParam(value = "tanggalSelesai", required = false) String tanggalSelesaiStr,
            Model model
    ) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Product> optionalProduct = productRepository.findById(idProduk);
        if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
            return "redirect:/user/home";
        }
        User user = optionalUser.get();
        Product product = optionalProduct.get();
        // Sinkronisasi data user jika ada perubahan data pengiriman
        boolean userChanged = false;
        if (!user.getNamaLengkap().equals(namaLengkap)) {
            user.setNamaLengkap(namaLengkap);
            userChanged = true;
        }
        if (!user.getNoTelepon().equals(noTelepon)) {
            user.setNoTelepon(noTelepon);
            userChanged = true;
        }
        if (!user.getAlamat().equals(alamat)) {
            user.setAlamat(alamat);
            userChanged = true;
        }
        if (userChanged) userRepository.save(user);

        if ("beli".equalsIgnoreCase(actionType)) {
            // Proses transaksi jual satu produk
            com.uaspbo.aljabar_outdoor.model.TransaksiJual transaksiJual = new com.uaspbo.aljabar_outdoor.model.TransaksiJual();
            transaksiJual.setUser(user);
            transaksiJual.setTanggalTransaksi(java.time.LocalDateTime.now());
            transaksiJual.setStatus(com.uaspbo.aljabar_outdoor.model.Transaksi.StatusTransaksi.Diproses);
            transaksiJual.setJenisTransaksi(com.uaspbo.aljabar_outdoor.model.Transaksi.JenisTransaksi.Beli);
            // Metode pembayaran
            try {
                transaksiJual.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiJual.MetodePembayaran.valueOf(metodePembayaranStr));
            } catch (Exception e) {
                transaksiJual.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiJual.MetodePembayaran.COD);
            }
            // Detail
            var detail = new com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual();
            detail.setTransaksiJual(transaksiJual);
            detail.setProduct(product);
            detail.setJumlah(jumlah);
            detail.setHargaSatuan(product.getHargaJual());
            detail.setSubtotal(product.getHargaJual().multiply(java.math.BigDecimal.valueOf(jumlah)));
            java.util.List<com.uaspbo.aljabar_outdoor.model.DetailTransaksiJual> detailList = new java.util.ArrayList<>();
            detailList.add(detail);
            transaksiJual.setDetailList(detailList);
            // Hitung total
            transaksiJual.setTotal(detail.getSubtotal());
            // Kurangi stok jual
            Integer stokJual = product.getStokJual() != null ? product.getStokJual() : 0;
            product.setStokJual(Math.max(0, stokJual - jumlah));
            productRepository.save(product);
            // Simpan transaksi
            transaksiRepository.save(transaksiJual);
            return "redirect:/user/transaksi/riwayat";
        } else if ("sewa".equalsIgnoreCase(actionType)) {
            // Proses transaksi sewa satu produk
            if (tanggalMulaiStr == null || tanggalSelesaiStr == null) {
                model.addAttribute("error", "Tanggal sewa harus diisi!");
                return "redirect:/user/product/" + idProduk;
            }
            java.time.LocalDate tanggalMulai = java.time.LocalDate.parse(tanggalMulaiStr);
            java.time.LocalDate tanggalSelesai = java.time.LocalDate.parse(tanggalSelesaiStr);
            long days = java.time.temporal.ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
            if (days < 1) days = 1;
            com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman transaksiPeminjaman = new com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman();
            transaksiPeminjaman.setUser(user);
            transaksiPeminjaman.setTanggalTransaksi(java.time.LocalDateTime.now());
            transaksiPeminjaman.setStatus(com.uaspbo.aljabar_outdoor.model.Transaksi.StatusTransaksi.Diproses);
            transaksiPeminjaman.setJenisTransaksi(com.uaspbo.aljabar_outdoor.model.Transaksi.JenisTransaksi.Sewa);
            transaksiPeminjaman.setTanggalMulai(tanggalMulai);
            transaksiPeminjaman.setTanggalSelesai(tanggalSelesai);
            // Metode pembayaran
            try {
                transaksiPeminjaman.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman.MetodePembayaran.valueOf(metodePembayaranStr));
            } catch (Exception e) {
                transaksiPeminjaman.setMetodePembayaran(com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman.MetodePembayaran.COD);
            }
            // Detail
            var detail = new com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman();
            detail.setTransaksiPeminjaman(transaksiPeminjaman);
            detail.setProduct(product);
            detail.setJumlah(jumlah);
            detail.setHargaPerHari(product.getHargaSewaPerHari());
            detail.setSubtotal(product.getHargaSewaPerHari().multiply(java.math.BigDecimal.valueOf(jumlah)));
            java.util.List<com.uaspbo.aljabar_outdoor.model.DetailTransaksiPeminjaman> detailList = new java.util.ArrayList<>();
            detailList.add(detail);
            transaksiPeminjaman.setDetailList(detailList);
            // Hitung total
            java.math.BigDecimal total = detail.getSubtotal().multiply(java.math.BigDecimal.valueOf(days));
            transaksiPeminjaman.setTotal(total);
            // Kurangi stok sewa
            Integer stokSewa = product.getStokSewa() != null ? product.getStokSewa() : 0;
            product.setStokSewa(Math.max(0, stokSewa - jumlah));
            productRepository.save(product);
            // Simpan transaksi
            transaksiRepository.save(transaksiPeminjaman);
            return "redirect:/user/transaksi/riwayat";
        }
        return "redirect:/user/home";
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

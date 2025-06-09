package com.uaspbo.aljabar_outdoor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uaspbo.aljabar_outdoor.model.Product;
import com.uaspbo.aljabar_outdoor.model.Transaksi;
import com.uaspbo.aljabar_outdoor.repository.ProductRepository;
import com.uaspbo.aljabar_outdoor.repository.TransaksiRepository;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long jumlahProduk = productRepository.count();
        long jumlahTransaksi = transaksiRepository.count();
        long jumlahJual = transaksiRepository.countByJenisTransaksi(Transaksi.JenisTransaksi.Beli);
        long jumlahSewa = transaksiRepository.countByJenisTransaksi(Transaksi.JenisTransaksi.Sewa);

        model.addAttribute("jumlahProduk", jumlahProduk);
        model.addAttribute("jumlahTransaksi", jumlahTransaksi);
        model.addAttribute("jumlahJual", jumlahJual);
        model.addAttribute("jumlahSewa", jumlahSewa);
        return "admin/dashboard";
    }   

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

    @PostMapping("/dataProduct/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Produk berhasil dihapus.");
        return "redirect:/admin/dataProduct";
    }

    @PostMapping("/dataProduct/edit/{id}")
    public String editProduct(
            @PathVariable("id") Integer id,
            @ModelAttribute Product product,
            RedirectAttributes redirectAttributes
    ) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNamaProduk(product.getNamaProduk());
            existing.setDeskripsi(product.getDeskripsi());
            existing.setHargaJual(product.getHargaJual());
            existing.setHargaSewaPerHari(product.getHargaSewaPerHari());
            existing.setStokJual(product.getStokJual());
            existing.setStokSewa(product.getStokSewa());
            existing.setKategori(product.getKategori());
            existing.setImgUrl(product.getImgUrl());
            productRepository.save(existing);
            redirectAttributes.addFlashAttribute("success", "Produk berhasil diupdate.");
        }
        return "redirect:/admin/dataProduct";
    }

    @GetMapping("/transaksi-penjualan")
    public String transaksiPenjualan(Model model) {
        model.addAttribute("diproses", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Beli, Transaksi.StatusTransaksi.Diproses));
        model.addAttribute("diterima", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Beli, Transaksi.StatusTransaksi.Diterima));
        model.addAttribute("dibatalkan", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Beli, Transaksi.StatusTransaksi.Dibatalkan));
        return "admin/transaksiPenjualan";
    }

    @GetMapping("/transaksi-penyewaan")
    public String transaksiPenyewaan(Model model) {
        model.addAttribute("diproses", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Sewa, Transaksi.StatusTransaksi.Diproses));
        model.addAttribute("diterima", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Sewa, Transaksi.StatusTransaksi.Diterima));
        model.addAttribute("dibatalkan", transaksiRepository.findByJenisTransaksiAndStatus(
            Transaksi.JenisTransaksi.Sewa, Transaksi.StatusTransaksi.Dibatalkan));
        return "admin/transaksiPenyewaan";
    }

    @PostMapping("/transaksi/update-status/{id}")
    public String updateTransaksiStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") Transaksi.StatusTransaksi status,
            RedirectAttributes redirectAttributes) {
        
        Transaksi transaksi = transaksiRepository.findById(id).orElse(null);
        if (transaksi != null) {
            transaksi.setStatus(status);
            transaksiRepository.save(transaksi);
            redirectAttributes.addFlashAttribute("success", "Status transaksi berhasil diupdate");
        }
        
        return "redirect:" + (transaksi.getJenisTransaksi() == Transaksi.JenisTransaksi.Beli ? 
                            "/admin/transaksi-penjualan" : "/admin/transaksi-penyewaan");
    }
}

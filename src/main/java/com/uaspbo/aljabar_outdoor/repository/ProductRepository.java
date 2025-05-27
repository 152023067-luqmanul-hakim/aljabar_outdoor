package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByOrderByHargaSewaPerHariAsc();
    List<Product> findAllByOrderByHargaSewaPerHariDesc();
    List<Product> findAllByOrderByIdProdukDesc();
    // Untuk terpopuler, tambahkan jika ada field popularitas
    List<Product> findByKategori(Product.Kategori kategori);
    List<Product> findByKategoriOrderByHargaSewaPerHariAsc(Product.Kategori kategori);
    List<Product> findByKategoriOrderByHargaSewaPerHariDesc(Product.Kategori kategori);
    List<Product> findByKategoriOrderByIdProdukDesc(Product.Kategori kategori);
}

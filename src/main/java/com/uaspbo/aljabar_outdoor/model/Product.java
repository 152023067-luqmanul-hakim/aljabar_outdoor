package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produk")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produk")
    private Integer idProduk;

    @Column(name = "nama_produk")
    private String namaProduk;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "harga_jual")
    private BigDecimal hargaJual;

    @Column(name = "harga_sewa_per_hari")
    private BigDecimal hargaSewaPerHari;

    @Column(name = "stok_jual")
    private Integer stokJual;

    @Column(name = "stok_sewa")
    private Integer stokSewa;

    public enum Kategori {
        Pakaian, Alat, Aksesoris
    }

    @Column(name = "kategori")
    @Enumerated(EnumType.STRING)
    private Kategori kategori;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;

    // Getter & Setter
    public Integer getIdProduk() { return idProduk; }
    public void setIdProduk(Integer idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public BigDecimal getHargaJual() { return hargaJual; }
    public void setHargaJual(BigDecimal hargaJual) { this.hargaJual = hargaJual; }

    public BigDecimal getHargaSewaPerHari() { return hargaSewaPerHari; }
    public void setHargaSewaPerHari(BigDecimal hargaSewaPerHari) { this.hargaSewaPerHari = hargaSewaPerHari; }

    public Integer getStokJual() { return stokJual; }
    public void setStokJual(Integer stokJual) { this.stokJual = stokJual; }

    public Integer getStokSewa() { return stokSewa; }
    public void setStokSewa(Integer stokSewa) { this.stokSewa = stokSewa; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
}
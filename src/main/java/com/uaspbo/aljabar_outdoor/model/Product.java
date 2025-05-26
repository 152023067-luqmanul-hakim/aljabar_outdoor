package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produk")
    private Integer idProduk;

    private String nama;
    private String deskripsi;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;

    @Column(name = "created_at", updatable = false, insertable = false)
    private java.sql.Timestamp createdAt;

    private String kategori;
    @Column(name = "harga_rental_per_hari")
    private Integer hargaRentalPerHari;
    @Column(name = "harga_jual")
    private Integer hargaJual;
    private Integer stok;

    // Getter & Setter
    public Integer getIdProduk() {
        return idProduk;
    }
    public void setIdProduk(Integer idProduk) {
        this.idProduk = idProduk;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    public Integer getHargaRentalPerHari() {
        return hargaRentalPerHari;
    }
    public void setHargaRentalPerHari(Integer hargaRentalPerHari) {
        this.hargaRentalPerHari = hargaRentalPerHari;
    }
    public Integer getHargaJual() {
        return hargaJual;
    }
    public void setHargaJual(Integer hargaJual) {
        this.hargaJual = hargaJual;
    }
    public Integer getStok() {
        return stok;
    }
    public void setStok(Integer stok) {
        this.stok = stok;
    }
}
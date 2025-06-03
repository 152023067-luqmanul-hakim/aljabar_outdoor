package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "keranjang")
public class Keranjang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_keranjang")
    private Integer idKeranjang;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "produk_id", referencedColumnName = "id_produk")
    private Product product;

    @Column(name = "jumlah")
    private Integer jumlah;

    @Column(name = "tanggal_ditambahkan")
    private LocalDateTime tanggalDitambahkan;

    public enum TipeAksi {
        BELI, SEWA
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipe_aksi")
    private TipeAksi tipeAksi; 

    public Integer getIdKeranjang() { return idKeranjang; }
    public void setIdKeranjang(Integer idKeranjang) { this.idKeranjang = idKeranjang; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public LocalDateTime getTanggalDitambahkan() { return tanggalDitambahkan; }
    public void setTanggalDitambahkan(LocalDateTime tanggalDitambahkan) { this.tanggalDitambahkan = tanggalDitambahkan; }

    public TipeAksi getTipeAksi() { return tipeAksi; }
    public void setTipeAksi(TipeAksi tipeAksi) { this.tipeAksi = tipeAksi; }
}
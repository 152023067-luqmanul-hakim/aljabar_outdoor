package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaksi_peminjaman")
public class TransaksiPeminjaman extends Transaksi {
    @ManyToOne
    @JoinColumn(name = "produk_id", referencedColumnName = "id_produk")
    private Product product;

    @Column(name = "harga_per_hari")
    private BigDecimal hargaPerHari;

    @Column(name = "tanggal_mulai")
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai")
    private LocalDate tanggalSelesai;

    // Getter & Setter
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public BigDecimal getHargaPerHari() { return hargaPerHari; }
    public void setHargaPerHari(BigDecimal hargaPerHari) { this.hargaPerHari = hargaPerHari; }

    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
}
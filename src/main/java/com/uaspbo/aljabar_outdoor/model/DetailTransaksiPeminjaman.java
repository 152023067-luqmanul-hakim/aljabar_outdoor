package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_peminjaman")
public class DetailTransaksiPeminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "peminjaman_id")
    private TransaksiPeminjaman transaksiPeminjaman;

    @ManyToOne
    @JoinColumn(name = "produk_id")
    private Product product;

    @Column(name = "jumlah")
    private Integer jumlah;

    @Column(name = "harga_per_hari")
    private BigDecimal hargaPerHari;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public TransaksiPeminjaman getTransaksiPeminjaman() { return transaksiPeminjaman; }
    public void setTransaksiPeminjaman(TransaksiPeminjaman transaksiPeminjaman) { this.transaksiPeminjaman = transaksiPeminjaman; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public BigDecimal getHargaPerHari() { return hargaPerHari; }
    public void setHargaPerHari(BigDecimal hargaPerHari) { this.hargaPerHari = hargaPerHari; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}

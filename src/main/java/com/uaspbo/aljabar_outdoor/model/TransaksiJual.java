package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaksi_jual")
public class TransaksiJual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "transaksi_id", referencedColumnName = "id_transaksi")
    private Transaksi transaksi;

    @ManyToOne
    @JoinColumn(name = "produk_id", referencedColumnName = "id_produk")
    private Product product;

    @Column(name = "jumlah")
    private Integer jumlah;

    @Column(name = "harga_satuan")
    private BigDecimal hargaSatuan;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(BigDecimal hargaSatuan) { this.hargaSatuan = hargaSatuan; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}

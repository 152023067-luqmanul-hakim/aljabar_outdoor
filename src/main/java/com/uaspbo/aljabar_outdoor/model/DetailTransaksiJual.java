package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_jual")
public class DetailTransaksiJual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "jual_id")
    private TransaksiJual transaksiJual;

    @ManyToOne
    @JoinColumn(name = "produk_id")
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

    public TransaksiJual getTransaksiJual() { return transaksiJual; }
    public void setTransaksiJual(TransaksiJual transaksiJual) { this.transaksiJual = transaksiJual; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(BigDecimal hargaSatuan) { this.hargaSatuan = hargaSatuan; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}

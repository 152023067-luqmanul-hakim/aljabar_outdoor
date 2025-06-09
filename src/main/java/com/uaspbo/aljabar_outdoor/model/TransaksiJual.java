package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaksi_jual")
public class TransaksiJual extends Transaksi {
    @ManyToOne
    @JoinColumn(name = "produk_id", referencedColumnName = "id_produk")
    private Product product;

    @Column(name = "harga_satuan")
    private BigDecimal hargaSatuan;

    // Getter & Setter
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(BigDecimal hargaSatuan) { this.hargaSatuan = hargaSatuan; }
}
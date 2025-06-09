package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaksi_jual")
public class TransaksiJual extends Transaksi {

    @OneToMany(mappedBy = "transaksiJual", cascade = CascadeType.ALL)
    private java.util.List<DetailTransaksiJual> detailList;

    @Column(name = "total")
    private BigDecimal total;

    public enum MetodePembayaran {
        COD, TRANSFER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "metode_pembayaran")
    private MetodePembayaran metodePembayaran;

    public java.util.List<DetailTransaksiJual> getDetailList() { return detailList; }
    public void setDetailList(java.util.List<DetailTransaksiJual> detailList) { this.detailList = detailList; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public MetodePembayaran getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(MetodePembayaran metodePembayaran) { this.metodePembayaran = metodePembayaran; }
}
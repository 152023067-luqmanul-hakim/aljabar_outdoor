package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaksi_peminjaman")
public class TransaksiPeminjaman extends Transaksi {

    @Column(name = "tanggal_mulai")
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai")
    private LocalDate tanggalSelesai;

    @Column(name = "total")
    private BigDecimal total;

    public enum MetodePembayaran {
        COD, TRANSFER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "metode_pembayaran")
    private MetodePembayaran metodePembayaran;

    @OneToMany(mappedBy = "transaksiPeminjaman", cascade = CascadeType.ALL)
    private java.util.List<DetailTransaksiPeminjaman> detailList;

    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public MetodePembayaran getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(MetodePembayaran metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public java.util.List<DetailTransaksiPeminjaman> getDetailList() { return detailList; }
    public void setDetailList(java.util.List<DetailTransaksiPeminjaman> detailList) { this.detailList = detailList; }
}
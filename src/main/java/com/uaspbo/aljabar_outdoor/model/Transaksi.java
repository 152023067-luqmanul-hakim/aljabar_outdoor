package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi")
public class Transaksi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaksi")
    private Integer idTransaksi;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    public enum JenisTransaksi {
        Sewa, Beli
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_transaksi")
    private JenisTransaksi jenisTransaksi;

    @Column(name = "tanggal_transaksi")
    private LocalDateTime tanggalTransaksi;

    public enum Status {
        Diproses, Dibatalkan, Diterima
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "total")
    private java.math.BigDecimal total;

    // Getter & Setter
    public Integer getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(Integer idTransaksi) { this.idTransaksi = idTransaksi; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public JenisTransaksi getJenisTransaksi() { return jenisTransaksi; }
    public void setJenisTransaksi(JenisTransaksi jenisTransaksi) { this.jenisTransaksi = jenisTransaksi; }

    public LocalDateTime getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public java.math.BigDecimal getTotal() { return total; }
    public void setTotal(java.math.BigDecimal total) { this.total = total; }
}

package com.uaspbo.aljabar_outdoor.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public enum StatusTransaksi {
        Diproses, Dibatalkan, Diterima
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_transaksi")
    private JenisTransaksi jenisTransaksi;

    @Column(name = "tanggal_transaksi")
    private LocalDateTime tanggalTransaksi;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusTransaksi status;

    // Getter & Setter
    public Integer getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(Integer idTransaksi) { this.idTransaksi = idTransaksi; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public JenisTransaksi getJenisTransaksi() { return jenisTransaksi; }
    public void setJenisTransaksi(JenisTransaksi jenisTransaksi) { this.jenisTransaksi = jenisTransaksi; }

    public LocalDateTime getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }

    public StatusTransaksi getStatus() { return status; }
    public void setStatus(StatusTransaksi status) { this.status = status; }
}
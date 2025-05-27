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

    @Column(name = "jenis_transaksi")
    private String jenisTransaksi;

    @Column(name = "tanggal_transaksi")
    private LocalDateTime tanggalTransaksi;

    @Column(name = "status")
    private String status;

    // Getter & Setter
    public Integer getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(Integer idTransaksi) { this.idTransaksi = idTransaksi; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getJenisTransaksi() { return jenisTransaksi; }
    public void setJenisTransaksi(String jenisTransaksi) { this.jenisTransaksi = jenisTransaksi; }

    public LocalDateTime getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

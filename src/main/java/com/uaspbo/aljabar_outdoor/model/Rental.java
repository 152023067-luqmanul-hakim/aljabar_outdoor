package com.uaspbo.aljabar_outdoor.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id_produk")
    private Product product;

    @Column(name = "tanggal_rental")
    private Date tanggalRental;

    @Column(name = "tanggal_pengembalian")
    private Date tanggalPengembalian;

    @Column(name = "total_harga")
    private Integer totalHarga;

    @Column(name = "tanggal_dikembalikan")
    private Date tanggalDikembalikan;

    @Column(name = "status")
    private String status;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Date getTanggalRental() { return tanggalRental; }
    public void setTanggalRental(Date tanggalRental) { this.tanggalRental = tanggalRental; }

    public Date getTanggalPengembalian() { return tanggalPengembalian; }
    public void setTanggalPengembalian(Date tanggalPengembalian) { this.tanggalPengembalian = tanggalPengembalian; }

    public Integer getTotalHarga() { return totalHarga; }
    public void setTotalHarga(Integer totalHarga) { this.totalHarga = totalHarga; }

    public Date getTanggalDikembalikan() { return tanggalDikembalikan; }
    public void setTanggalDikembalikan(Date tanggalDikembalikan) { this.tanggalDikembalikan = tanggalDikembalikan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaksiPeminjamanRepository extends JpaRepository<TransaksiPeminjaman, Integer> {
    List<TransaksiPeminjaman> findByTransaksi_IdTransaksi(Integer idTransaksi);
    // Jika tetap error, gunakan @Query sebagai alternatif:
    // @org.springframework.data.jpa.repository.Query("SELECT t FROM TransaksiPeminjaman t WHERE t.transaksi.idTransaksi = :idTransaksi")
    // List<TransaksiPeminjaman> findByTransaksiIdTransaksi(@org.springframework.data.repository.query.Param("idTransaksi") Integer idTransaksi);
}

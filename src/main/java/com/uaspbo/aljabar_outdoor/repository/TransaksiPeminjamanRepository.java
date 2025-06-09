package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.TransaksiPeminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransaksiPeminjamanRepository extends JpaRepository<TransaksiPeminjaman, Integer> {
    // Tidak perlu override findById, sudah ada di JpaRepository
    // Jika ingin custom query, gunakan @Query
    // Contoh:
    // @Query("SELECT t FROM TransaksiPeminjaman t WHERE t.id = :idTransaksi")
    // Optional<TransaksiPeminjaman> findByTransaksiId(@Param("idTransaksi") Integer idTransaksi);
}

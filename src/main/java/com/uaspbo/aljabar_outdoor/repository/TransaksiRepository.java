package com.uaspbo.aljabar_outdoor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uaspbo.aljabar_outdoor.model.Transaksi;

public interface TransaksiRepository extends JpaRepository<Transaksi, Integer> {
    List<Transaksi> findByUserIdUser(Integer userId);
    long countByJenisTransaksi(Transaksi.JenisTransaksi jenisTransaksi);
    List<Transaksi> findByJenisTransaksi(Transaksi.JenisTransaksi jenisTransaksi);
}

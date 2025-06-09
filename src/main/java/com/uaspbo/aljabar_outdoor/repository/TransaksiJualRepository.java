package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.TransaksiJual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaksiJualRepository extends JpaRepository<TransaksiJual, Integer> {
    // List<TransaksiJual> findByTransaksi_IdTransaksi(Integer idTransaksi);

    // Jika tetap error, gunakan @Query sebagai alternatif:
    // @Query("SELECT t FROM TransaksiJual t WHERE t.transaksi.idTransaksi = :idTransaksi")
    // List<TransaksiJual> findByTransaksiIdTransaksi(@Param("idTransaksi") Integer idTransaksi);
}

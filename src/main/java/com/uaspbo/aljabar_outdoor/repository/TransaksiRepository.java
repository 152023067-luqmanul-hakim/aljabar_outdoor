package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, Integer> {
    List<Transaksi> findByUserIdUser(Integer userId);
}

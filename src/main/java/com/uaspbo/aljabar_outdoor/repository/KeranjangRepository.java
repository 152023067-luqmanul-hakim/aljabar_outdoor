package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.Keranjang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KeranjangRepository extends JpaRepository<Keranjang, Integer> {
    List<Keranjang> findByUserIdUser(Integer userId);
    Keranjang findByUserIdUserAndProductIdProdukAndTipeAksi(Integer userId, Integer produkId, Keranjang.TipeAksi tipeAksi);
}

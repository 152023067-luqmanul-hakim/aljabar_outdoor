package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.Keranjang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KeranjangRepository extends JpaRepository<Keranjang, Integer> {
    List<Keranjang> findByUserIdUser(Integer idUser);
    Keranjang findByUserIdUserAndProductIdProduk(Integer idUser, Integer idProduk);
}

package com.duoc.Edutech.repository;

import com.duoc.Edutech.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Integer> {
    @Query("SELECT c FROM Cupon c LEFT JOIN FETCH c.pago WHERE c.idcupon = :idcupon")
    Optional<Cupon> findByIdConpago(@Param("idcupon") Integer idcupon);
}
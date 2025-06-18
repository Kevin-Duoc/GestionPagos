package com.duoc.Edutech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.Edutech.model.Pago;
@Repository
public interface PagoRepository extends JpaRepository <Pago,Integer> {

}

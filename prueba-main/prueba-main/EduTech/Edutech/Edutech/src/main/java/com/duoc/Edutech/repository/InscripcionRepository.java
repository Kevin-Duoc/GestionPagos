package com.duoc.Edutech.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.Edutech.model.Inscripcion;
@Repository
public interface InscripcionRepository extends JpaRepository <Inscripcion,Integer> {

    public Inscripcion findByRut(String rut);
}

package com.duoc.Edutech.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Inscripcion")
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInscripcion;

    @Column(nullable = false)
    @JsonProperty("fechaInscripcion")
    private LocalDate fechaInscripcion;

    @Column(nullable = false)
    @JsonProperty("EstadoInscripcion")
    private String EstadoInscripcion;

    @Column(nullable = false)
    private String rut;


}
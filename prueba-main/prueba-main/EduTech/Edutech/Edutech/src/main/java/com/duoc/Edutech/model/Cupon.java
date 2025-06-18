package com.duoc.Edutech.model;
import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Data
@Entity
@Table (name="Cupon")
@NoArgsConstructor
@AllArgsConstructor

public class Cupon {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer idcupon;

@Column(unique=true,nullable=false)
private String codigo;

@Column(nullable = false)
private Integer descuento;

@Column (nullable = false)
private LocalDate fecha_vencimiento;

@Column(nullable = false)
private String estado;

@ManyToOne
@JoinColumn(name = "idpago")
@OnDelete(action = OnDeleteAction.CASCADE)
private Pago pago;
}

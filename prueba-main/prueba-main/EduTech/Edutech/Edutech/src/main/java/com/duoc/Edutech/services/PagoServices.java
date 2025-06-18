package com.duoc.Edutech.services;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.repository.InscripcionRepository;
import org.aspectj.weaver.patterns.IfPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.PagoRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@Transactional
public class PagoServices {
    
    @Autowired
    private PagoRepository pagoRepository;


    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private CuponServices cuponServices;

   public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Optional<Pago> findById(Integer idPago) {
        Optional<Pago> pago = pagoRepository.findById(idPago);

        if (pago.isPresent()) {
            System.out.println("Pago encontrado: " + pago.get());
        } else {
            System.out.println("No se encontró pago con ID: " + idPago);
        }

        return pago;
    }



public String eliminarPago(Integer idPago) {
    return pagoRepository.findById(idPago)
            .map(pago -> {
                pagoRepository.deleteById(idPago);
                return "Pago eliminado con éxito";
            })
            .orElse("No se encontró pago con ID: " + idPago);
}

        public Map<String, Object> pagarConCupon(Integer idInscripcion, Integer idCupon, Pago pagoNuevo) {
            Map<String, Object> response = new LinkedHashMap<>();

            Optional<Inscripcion> inscripcion = inscripcionRepository.findById(idInscripcion);
            if (inscripcion.isEmpty()) {
                response.put("error", "Inscripción no encontrada");
                return response;
            }

            Optional<Cupon> cuponOpt = cuponServices.findById(idCupon);
            if (cuponOpt.isEmpty()) {
                response.put("error", "El cupón no existe");
                return response;
            }

            Cupon cupon = cuponOpt.get();
            if (cupon.getEstado().equals("usado") || cupon.getEstado().equals("canjeado")) {
                response.put("error", "El cupón está vencido/canjeado");
                return response;
            }

            if (pagoNuevo.getMontoPagos() == null) {
                response.put("error", "El monto a pagar es necesario");
                return response;
            }

            pagoNuevo.setFecha_pago(LocalDate.now());
            pagoNuevo.setInscripcion(inscripcion.get());
            double montoConDescuento = pagoNuevo.getMonto() - (pagoNuevo.getMonto() * cupon.getDescuento() / 100);

            if (pagoNuevo.getMontoPagos() >= montoConDescuento) {
                pagoNuevo.setEstado("aprobado");
                pagoNuevo.getInscripcion().setEstadoInscripcion("pagado");
                cupon.setEstado("usado");

                Pago pagoGuardado = save(pagoNuevo);
                cupon.setPago(pagoGuardado);
                cuponServices.save(cupon);
                
                response.put("descuento Aplicado", cupon.getDescuento());
                response.put("monto a pagar", pagoNuevo.getMonto());
                response.put("monto final a pagar", montoConDescuento);
                response.put("pago", pagoGuardado);
            } else {
                pagoNuevo.setEstado("rechazado");
                pagoNuevo.getInscripcion().setEstadoInscripcion("no pagado");
                
                Pago pagoGuardado = save(pagoNuevo);
                response.put("error", "El monto pagado es menor al monto con descuento");
                response.put("pago", pagoGuardado);
            }

            return response;
        }
public Pago pagarSinCupon(Integer idInscripcion, Pago pagoNuevo) {
    Optional<Inscripcion> inscripcion = inscripcionRepository.findById(idInscripcion);
    if (inscripcion.isEmpty()) {
        return null;
    }

    if (pagoNuevo.getMontoPagos() == null) {
        return null;
    }
    
    pagoNuevo.setFecha_pago(LocalDate.now());
    pagoNuevo.setInscripcion(inscripcion.get());
    
    if (pagoNuevo.getMontoPagos() >= pagoNuevo.getMonto()) {
        pagoNuevo.setEstado("aprobado");
        pagoNuevo.getInscripcion().setEstadoInscripcion("pagado");
    } else {
        pagoNuevo.setEstado("rechazado");
        pagoNuevo.getInscripcion().setEstadoInscripcion("no pagado");
    }

    return save(pagoNuevo);
}
    }
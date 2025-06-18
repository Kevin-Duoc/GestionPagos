package com.duoc.Edutech.services;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.CuponRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@Transactional
public class CuponServices {

    @Autowired
    private CuponRepository cuponRepository;

    public Cupon crearCupon(Cupon cupon) {

        LocalDate fechaVencimiento = LocalDate.now().plusDays(5);
        cupon.setFecha_vencimiento(fechaVencimiento);


        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigoBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            codigoBuilder.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        cupon.setCodigo(codigoBuilder.toString());

        if (fechaVencimiento.isAfter(LocalDate.now())) {
            cupon.setEstado("vigente");
        } else {
            cupon.setEstado("vencido");
        }

        return save(cupon);
    }

    public Cupon save(Cupon cupon) {
        return cuponRepository.save(cupon);
    }

    public void deleteById(Integer idcupon) {
        cuponRepository.deleteById(idcupon);
    }

public Optional<Cupon> findById(Integer idcupon) {
    return cuponRepository.findById(idcupon);
}

public Map<String, Object> findByIdConpago(Integer idcupon) {
    Map<String, Object> response = new LinkedHashMap<>();
    
    Optional<Cupon> cupon = cuponRepository.findByIdConpago(idcupon);
    if (cupon.isPresent()) {
        Map<String, Object> cuponInfo = new LinkedHashMap<>();
        cuponInfo.put("idcupon", cupon.get().getIdcupon());
        cuponInfo.put("codigo", cupon.get().getCodigo());
        cuponInfo.put("descuento", cupon.get().getDescuento());
        cuponInfo.put("fecha_vencimiento", cupon.get().getFecha_vencimiento());
        cuponInfo.put("estado", cupon.get().getEstado());
        
        response.put("cupon", cuponInfo);
        
        if (cupon.get().getPago() != null) {
            response.put("pago", cupon.get().getPago());
        } else {
            response.put("pago", "No hay pago asociado a este cupón");
        }
    }
    
    return response;
}
}
package com.duoc.Edutech.services;

import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class InscripcionServices {

    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private RestTemplate restTemplate;
    public Inscripcion save(Inscripcion inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion deleteById(Inscripcion inscripcion) {
        Inscripcion inscripcionExistente = inscripcionRepository.findById(inscripcion.getIdInscripcion())
                .orElse(null);
        if (inscripcionExistente != null) {
            inscripcionRepository.deleteById(inscripcion.getIdInscripcion());
        }
        return inscripcionExistente;
    }
    public Inscripcion findById(Integer idInscripcion) {
        return inscripcionRepository.findById(idInscripcion).orElse(null);
    }
    public String buscarporrut(String rut) {
        String alumnourl = "http://localhost:8082/api/v1/estudiantes/"+rut;
        String alumnodata = restTemplate.getForObject(alumnourl, String.class);
        Inscripcion inscripcion = inscripcionRepository.findByRut(rut);
        if (inscripcion == null) {
            return "No se encontro inscripcion para el alumno con RUN: " + rut;
        }else{
            StringBuilder resultado = new StringBuilder();
            resultado.append("Curso: \n");
            resultado.append("\n -Curso.").append(inscripcion.getIdInscripcion())
                    .append("\n -Descripcion.").append(inscripcion.getEstadoInscripcion())
                    .append("\n -Estado.").append(inscripcion.getFechaInscripcion());
            resultado.append("Instructor: \n");
            resultado.append(alumnodata);
            return resultado.toString();
        }
    }

    // @GetMapping("/buscarporruta/{rut}")
    // public String buscarporruta(@PathVariable String rut) {
    //     return restTemplate.getForObject("http://localhost:8080/api/rutaEdutech/{rut}", String.class, rut);
    // }
    // @GetMapping("/buscarporruta/{rut}")
    // public String buscarporruta(@PathVariable String rut) {
    //     return restTemplate.getForObject("http://localhost:8080/api/rutaEdutech/{rut}", String.class, rut);

}
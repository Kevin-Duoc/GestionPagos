package com.duoc.Edutech;

import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.repository.InscripcionRepository;
import com.duoc.Edutech.services.InscripcionServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InscripcionServicesTest {
    @Mock
    private InscripcionRepository inscripcionRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private InscripcionServices inscripcionServicesTest;

    private Inscripcion inscripcion;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.inscripcion = new Inscripcion();
        this.inscripcion.setIdInscripcion(1);
        this.inscripcion.setEstadoInscripcion("activa");
        this.inscripcion.setRut("111111");
    }
    @Test
    public void testGuardarInscripcion(){
        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setIdInscripcion(1);

        inscripcion.setEstadoInscripcion("activa");

        when(inscripcionRepository.save(inscripcion)).thenReturn(inscripcion);

        Inscripcion inscripcionGuardada = inscripcionServicesTest.save(inscripcion);
        assertNotNull(inscripcionGuardada);
        assertEquals(1,inscripcionGuardada.getIdInscripcion());
        assertEquals("activa",inscripcionGuardada.getEstadoInscripcion());
        verify(inscripcionRepository,times(1)).save(inscripcion);
    }
    @Test
    public void testBorrarInscripcion(){
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        Inscripcion resultado = inscripcionServicesTest.deleteById(inscripcion);
        verify(inscripcionRepository).deleteById(1);
        assertNotNull(resultado);
        assertEquals(inscripcion,resultado);
    }
    @Test
    public void testFindByIdInscripcion(){
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        Inscripcion resultado = inscripcionServicesTest.findById(1);
        assertNotNull(resultado);
        assertNotNull(resultado.getIdInscripcion());
        verify(inscripcionRepository).findById(1);
    }
    @Test
    public void testBuscarPorRutCuandoExisteInscripcion(){
        when(restTemplate.getForObject(
                "http://localhost:8082/api/v1/estudiantes/111111",
                String.class
        )).thenReturn("Datos del alumno");

        when(inscripcionRepository.findByRut("111111")).thenReturn(inscripcion);

        String resultado = inscripcionServicesTest.buscarporrut("111111");

        assertTrue(resultado.contains("Curso"));
        assertTrue(resultado.contains("Datos del alumno"));
        verify(inscripcionRepository).findByRut("111111");
    }


}
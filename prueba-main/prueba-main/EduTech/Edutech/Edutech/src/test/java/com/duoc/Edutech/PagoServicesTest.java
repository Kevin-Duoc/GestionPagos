package com.duoc.Edutech;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.InscripcionRepository;
import com.duoc.Edutech.repository.PagoRepository;
import com.duoc.Edutech.services.CuponServices;
import com.duoc.Edutech.services.PagoServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PagoServicesTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private CuponServices cuponServices;

    @InjectMocks
    private PagoServices pagoServices;

    private Pago pago;
    private Inscripcion inscripcion;
    private Cupon cupon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pago = new Pago();
        pago.setIdpago(1);
        pago.setMonto(1000);
        pago.setMontoPagos(1000);
        pago.setEstado("pendiente");
        pago.setMetodopago("tarjeta");
        pago.setFecha_pago(LocalDate.now());

        inscripcion = new Inscripcion();
        inscripcion.setIdInscripcion(1);
        inscripcion.setEstadoInscripcion("pendiente");

        cupon = new Cupon();
        cupon.setIdcupon(1);
        cupon.setDescuento(10);
        cupon.setEstado("vigente");
    }

    @Test
    void testSave() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Pago resultado = pagoServices.save(pago);

        assertNotNull(resultado);
        assertEquals(pago.getIdpago(), resultado.getIdpago());
        verify(pagoRepository).save(pago);
    }

    @Test
    void testFindByIdExistente() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        Optional<Pago> resultado = pagoServices.findById(1);

        assertTrue(resultado.isPresent());
        assertEquals(pago.getIdpago(), resultado.get().getIdpago());
        verify(pagoRepository).findById(1);
    }

    @Test
    void testFindByIdNoExistente() {
        when(pagoRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Pago> resultado = pagoServices.findById(1);

        assertFalse(resultado.isPresent());
        verify(pagoRepository).findById(1);
    }

    @Test
    void testEliminarPagoExistente() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        doNothing().when(pagoRepository).deleteById(1);

        String resultado = pagoServices.eliminarPago(1);

        assertEquals("Pago eliminado con éxito", resultado);
        verify(pagoRepository).findById(1);
        verify(pagoRepository).deleteById(1);
    }

    @Test
    void testEliminarPagoNoExistente() {
        when(pagoRepository.findById(999)).thenReturn(Optional.empty());

        String resultado = pagoServices.eliminarPago(999);

        assertEquals("No se encontró pago con ID: 999", resultado);
        verify(pagoRepository).findById(999);
        verify(pagoRepository, never()).deleteById(anyInt());
    }

    @Test
    void testPagarConCuponExitoso() {
        pago.setInscripcion(inscripcion);
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        when(cuponServices.findById(1)).thenReturn(Optional.of(cupon));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Map<String, Object> resultado = pagoServices.pagarConCupon(1, 1, pago);

        assertNotNull(resultado);
        assertFalse(resultado.containsKey("error"));
        assertTrue(resultado.containsKey("pago"));
        assertEquals("aprobado", pago.getEstado());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void testPagarConCuponInscripcionNoEncontrada() {
        when(inscripcionRepository.findById(2)).thenReturn(Optional.empty());

        Map<String, Object> resultado = pagoServices.pagarConCupon(2, 1, pago);

        assertTrue(resultado.containsKey("error"));
        assertEquals("Inscripción no encontrada", resultado.get("error"));
    }

    @Test
    void testPagarSinCuponExitoso() {
        pago.setMontoPagos(1000);
        pago.setMonto(1000);
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Pago resultado = pagoServices.pagarSinCupon(1, pago);

        assertNotNull(resultado);
        assertEquals("aprobado", resultado.getEstado());
        assertEquals("pagado", resultado.getInscripcion().getEstadoInscripcion());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void testPagarSinCuponMontoInsuficiente() {
        pago.setMontoPagos(500);
        pago.setMonto(1000);
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Pago resultado = pagoServices.pagarSinCupon(1, pago);

        assertNotNull(resultado);
        assertEquals("rechazado", resultado.getEstado());
        assertEquals("no pagado", resultado.getInscripcion().getEstadoInscripcion());
        verify(pagoRepository).save(any(Pago.class));
    }
}

package com.duoc.Edutech;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.CuponRepository;
import com.duoc.Edutech.services.CuponServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CuponServicesTest {

    @Mock
    private CuponRepository cuponRepository;

    @InjectMocks
    private CuponServices cuponServices;

    private Cupon cupon;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        cupon = new Cupon();
        cupon.setIdcupon(1);
        cupon.setDescuento(10);
        cupon.setEstado("vigente");
    }
    @Test
    public void testCrearCupon(){
        Cupon cupon = new Cupon();
        when(cuponRepository.save(any(Cupon.class))).thenReturn(cupon);

        Cupon resultadocup = cuponServices.crearCupon(cupon);

        assertNotNull(resultadocup);
        assertEquals("vigente",resultadocup.getEstado());
        assertNotNull(resultadocup.getCodigo());
        assertEquals(6, resultadocup.getCodigo().length());
        assertTrue(resultadocup.getFecha_vencimiento().isAfter(LocalDate.now()));
        verify(cuponRepository).save(any(Cupon.class));
}

    @Test
    public void testGuardarCupon(){
    when (cuponRepository.save(cupon)).thenReturn(cupon);
    Cupon resultado = cuponServices.save(cupon);
    assertNotNull(resultado);
    assertEquals(cupon.getIdcupon(),resultado.getIdcupon());
    verify(cuponRepository).save(cupon);
    }

    @Test
    public void testFindbyId(){
        when (cuponRepository.findById(1)).thenReturn(Optional.of(cupon));

        Optional<Cupon> resultadoCupon = cuponServices.findById(1);
        assertTrue(resultadoCupon.isPresent());
        assertEquals(1,resultadoCupon.get().getIdcupon());
        verify(cuponRepository).findById(1);
    }

    @Test
    public void testNohayCupon(){
        when (cuponRepository.findByIdConpago(1)).thenReturn(Optional.empty());

        Map<String, Object> resultadocuppago = cuponServices.findByIdConpago(1);
        assertTrue(resultadocuppago.isEmpty());
        verify(cuponRepository).findByIdConpago(1);
    }
    @Test
    public void TestFindbyIdCuponConpago (){
        Pago pago = new Pago();
        pago.setIdpago(1);
        cupon.setPago(pago);

        when(cuponRepository.findByIdConpago(1)).thenReturn(Optional.of(cupon));

        Map<String, Object> resultadoConPago = cuponServices.findByIdConpago(1);

        assertNotNull(resultadoConPago);
        assertTrue(resultadoConPago.containsKey("cupon"));
        assertTrue(resultadoConPago.containsKey("pago"));
        assertNotEquals("No hay pago asociado a este cupón", resultadoConPago.get("pago"));

        verify(cuponRepository).findByIdConpago(1);
    }

    @Test
    public void TestFindByIdCuponSinpago(){
        cupon.setPago(null);
        when(cuponRepository.findByIdConpago(1)).thenReturn(Optional.of(cupon));
        Map<String, Object> resultado = cuponServices.findByIdConpago(1);
        assertNotNull(resultado);
        assertTrue(resultado.containsKey("cupon"));
        assertTrue(resultado.containsKey("pago"));
        assertEquals("No hay pago asociado a este cupón", resultado.get("pago"));
        verify(cuponRepository).findByIdConpago(1);

    }
    @Test
    public void testDeleteById(){
        doNothing().when(cuponRepository).deleteById(1);
        cuponServices.deleteById(1);
        verify(cuponRepository).deleteById(1);
    }
}


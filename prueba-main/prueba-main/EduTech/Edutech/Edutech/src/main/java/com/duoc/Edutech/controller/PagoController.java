package com.duoc.Edutech.controller;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.InscripcionRepository;
import com.duoc.Edutech.services.CuponServices;
import com.duoc.Edutech.services.PagoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/v1/pagoEdutech")
public class PagoController {

    @Autowired
    private PagoServices pagoServices;
    @Operation(summary = "consultar pago ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pago encontrado exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "400", description = "No se encontro el pago especificado!!",
                    content = @Content)
    })
    @GetMapping("/consultarpago/{idpago}")
    public ResponseEntity<?> consultarpago(@Parameter(description = "ID del cupon", required = true, example = "1")
                                               @PathVariable Integer idpago) {
        return pagoServices.findById(idpago)
                .map(pago -> ResponseEntity.ok(pago))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(summary = "Eliminar pago ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " pago eliminado exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "404", description = "No se elimino el pago especificado!!",
                    content = @Content)
    })
    @DeleteMapping("/eliminarpago/{idpago}")
    public ResponseEntity<?> eliminarpago(@Parameter(description = "ID del pago", required = true, example = "1")
                                              @PathVariable Integer idpago) {
        String resultado = pagoServices.eliminarPago(idpago);
        return resultado != null ?
            ResponseEntity.ok(resultado) :
            ResponseEntity.notFound().build();

    }
@Operation(summary = "Realizar pago con cupón de descuento")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pago realizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))),
    @ApiResponse(responseCode = "400", description = "Error en el proceso de pago",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
})
@PostMapping("/pagarconcupon/{idInscripcion}/{idCupon}")
public ResponseEntity<?> pagarConCupon(
        @Parameter(description = "ID de la inscripción", required = true, example = "1")
        @PathVariable Integer idInscripcion,

        @Parameter(description = "ID del cupón de descuento", required = true, example = "1")
        @PathVariable Integer idCupon,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del pago",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = Pago.class),
                        examples = @ExampleObject(value = """
                                {
                                    "monto": 50000,
                                    "montoPagos": 40000,
                                    "metodopago": "transferencia",
                                    "estado": "pendiente"
                                }
                                """)
                )
        )
        @RequestBody Pago pagoNuevo) {
    Map<String, Object> resultado = pagoServices.pagarConCupon(idInscripcion, idCupon, pagoNuevo);
    if (resultado.containsKey("error")) {
        return ResponseEntity.badRequest().body(resultado);
    }

    return ResponseEntity.ok(resultado);
}

@Operation(summary = "Realizar pago sin cupón de descuento")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pago realizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Pago.class))),
    @ApiResponse(responseCode = "400", description = "Error en el proceso de pago",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string")))
})
@PostMapping("/pagarsincupon/{idInscripcion}")
public ResponseEntity<?> pagarcurso(
        @Parameter(description = "ID de la inscripción", required = true, example = "1")
        @PathVariable Integer idInscripcion,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del pago",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = Pago.class),
                        examples = @ExampleObject(value = """
                                {
                                    "monto": 50000,
                                    "montoPagos": 50000,
                                    "metodopago": "transferencia",
                                    "estado": "pendiente"
                                }
                                """)
                )
        )
        @RequestBody Pago pagoNuevo) {

    Pago pagoRealizado = pagoServices.pagarSinCupon(idInscripcion, pagoNuevo);

    if (pagoRealizado == null) {
        return ResponseEntity.badRequest().body("Error al procesar el pago. Verifique los datos.");
    }

    return ResponseEntity.ok(pagoRealizado);
}
}
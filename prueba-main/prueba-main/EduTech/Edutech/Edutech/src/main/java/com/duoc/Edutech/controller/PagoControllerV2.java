package com.duoc.Edutech.controller;

import com.duoc.Edutech.Assemblers.PagoModelAssembler;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/pagoEdutech")
public class PagoControllerV2 {

    @Autowired
    private PagoServices pagoServices;
    
    @Autowired
    private PagoModelAssembler pagoModelAssembler;

    @Operation(summary = "consultar pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pago encontrado exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "400", description = "No se encontro el pago especificado!!",
                    content = @Content)
    })
    @GetMapping("/consultarpago/{idpago}")
    public ResponseEntity<?> consultarpago(@Parameter(description = "ID del pago", required = true, example = "1")
                                         @PathVariable Integer idpago) {
        return pagoServices.findById(idpago)
                .map(pago -> ResponseEntity.ok(pagoModelAssembler.toModel(pago)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pago eliminado exitosamente!!"),
            @ApiResponse(responseCode = "404", description = "No se elimino el pago especificado!!")
    })
    @DeleteMapping("/eliminarpago/{idpago}")
    public ResponseEntity<?> eliminarpago(@Parameter(description = "ID del pago", required = true, example = "1")
                                        @PathVariable Integer idpago) {
        String resultado = pagoServices.eliminarPago(idpago);
        if (resultado.startsWith("No se encontró")) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", resultado);
        
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(PagoControllerV2.class).consultarpago(idpago)).withRel("verificar eliminación"),
                linkTo(methodOn(PagoControllerV2.class).pagarSinCupon(null, null)).withRel("realizar nuevo pago")));
    }

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

        if (resultado.containsKey("pago")) {
            Pago pagoRealizado = (Pago) resultado.get("pago");
            EntityModel<Pago> pagoModel = pagoModelAssembler.toModel(pagoRealizado);
            resultado.put("pago", pagoModel);
        }

        return ResponseEntity.ok(EntityModel.of(resultado));
    }


    @PostMapping("/pagarsincupon/{idInscripcion}")
    public ResponseEntity<?> pagarSinCupon(
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
            return ResponseEntity.badRequest().body(
                    EntityModel.of(
                            Map.of("error", "Error al procesar el pago. Verifique los datos."),
                            linkTo(methodOn(PagoControllerV2.class).pagarSinCupon(idInscripcion, null))
                                    .withRel("reintentar pago")
                    )
            );
        }

        return ResponseEntity.ok(pagoModelAssembler.toModel(pagoRealizado));
    }

}
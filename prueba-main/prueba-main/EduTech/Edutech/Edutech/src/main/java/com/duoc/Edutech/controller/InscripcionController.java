package com.duoc.Edutech.controller;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.services.InscripcionServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/Inscripcion")
public class InscripcionController {
    @Autowired
    private InscripcionServices inscripcionServices;

@Operation(summary = "Crear nueva inscripción")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Inscripción creada exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Inscripcion.class))),
    @ApiResponse(responseCode = "400", description = "Datos de inscripción inválidos",
            content = @Content)
})
@PostMapping("/crear")
public ResponseEntity<Inscripcion> CrearInscripcion(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la nueva inscripción",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = Inscripcion.class),
                        examples = @ExampleObject(value = """
                                {
                                    "EstadoInscripcion": "pendiente",
                                    "rut": "12345678-9"
                                }
                                """)
                )
        )
        @RequestBody Inscripcion inscripcion) {
    inscripcion.setFechaInscripcion(LocalDate.now());
    return ResponseEntity.ok(inscripcionServices.save(inscripcion));
}
    @Operation(summary = " borrar inscripcion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "inscripcion borrada exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "400", description = "No se logro eliminar la inscripcion especificada!!",
                    content = @Content)
    })
    @DeleteMapping("/borrar/{idInscripcion}")
    public ResponseEntity<Inscripcion> deleteById(@Parameter(description = "ID de la inscripcion", required = true, example = "1")
                                                      @PathVariable Integer idInscripcion) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdInscripcion(idInscripcion);
        return ResponseEntity.ok(inscripcionServices.deleteById(inscripcion));
    }
    @Operation(summary = " buscar inscripcion por rut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "inscripcion encontrada exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "400", description = "No se logro encontrar la inscripcion especificada!!",
                    content = @Content)
    })
    @GetMapping("/buscarporrut/{rut}")
    public ResponseEntity<String> buscarporrut(@Parameter(description = "ID de la inscripcion", required = true, example = "1")
                                                   @PathVariable String rut) {
        return ResponseEntity.ok(inscripcionServices.buscarporrut(rut));
    }
}
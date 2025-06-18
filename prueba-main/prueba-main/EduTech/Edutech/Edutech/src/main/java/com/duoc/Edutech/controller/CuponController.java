package com.duoc.Edutech.controller;
import java.time.LocalDate;
import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.repository.CuponRepository;
import com.duoc.Edutech.services.CuponServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Random;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Cupon")
public class CuponController {

    @Autowired
    private CuponServices cuponServices;
    @Operation(summary = "Crear nuevo cupón")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupón creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "400", description = "Datos del cupón inválidos",
                    content = @Content)
    })
    @PostMapping("/crearCupon")

    public ResponseEntity<Cupon> crearCupon(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del nuevo cupón",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = Cupon.class),
                        examples = @ExampleObject(value = """
                                {
                                    "descuento": 20
                                }
                                """)
                )
        )
        @RequestBody Cupon cupon) {
    return ResponseEntity.ok(cuponServices.crearCupon(cupon));
}


    @Operation(summary = "Consultar cupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupon consultado exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró el cupon especificado!!",
                    content = @Content)
    })
@GetMapping("/buscarporid/{idCupon}")
    public ResponseEntity<?> buscarporid(@Parameter(description = "ID del cupon", required = true, example = "1")
                                             @PathVariable Integer idCupon) {
        Map<String, Object> resultado = cuponServices.findByIdConpago(idCupon);
        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }
    @Operation(summary = "eliminar cupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupon eliminad exitosamente!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cupon.class))),
            @ApiResponse(responseCode = "404", description = "No se elimino el cupon especificado!!",
                    content = @Content)
    })
    @DeleteMapping("/borrarCupon/{idCupon}")
    public ResponseEntity<?> deleteById(@Parameter(description = "ID del cupon", required = true, example = "1")
                                            @PathVariable Integer idCupon) {
        return cuponServices.findById(idCupon).map(cupon -> {
            cuponServices.deleteById(idCupon);
            return ResponseEntity.ok("Cupon eliminado con éxito");
        }).orElseGet(() -> {
            return ResponseEntity.notFound().build();
        });
    }
}
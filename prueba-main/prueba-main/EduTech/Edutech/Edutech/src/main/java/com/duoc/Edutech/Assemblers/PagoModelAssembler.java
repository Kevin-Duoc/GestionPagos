package com.duoc.Edutech.Assemblers;

import com.duoc.Edutech.controller.CuponControllerV2;
import com.duoc.Edutech.controller.PagoController;
import com.duoc.Edutech.controller.PagoControllerV2;
import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.model.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {

    @Override
    public EntityModel<Pago> toModel(Pago pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagoControllerV2.class).consultarpago(pago.getIdpago())).withRel("consultar pago"),

                linkTo(methodOn(PagoControllerV2.class).eliminarpago(pago.getIdpago())).withRel("eliminar pago"),

                linkTo(methodOn(PagoControllerV2.class)
                        .pagarConCupon(
                            pago.getInscripcion() != null ? pago.getInscripcion().getIdInscripcion() : null,
                            null,
                            null))
                        .withRel("pagar con cupón"),
                linkTo(methodOn(PagoControllerV2.class)
                        .pagarSinCupon(
                            pago.getInscripcion() != null ? pago.getInscripcion().getIdInscripcion() : null,
                            null))
                        .withRel("pagar sin cupón")
        );
    }
}
package com.duoc.Edutech.Assemblers;

import com.duoc.Edutech.controller.CuponController;
import com.duoc.Edutech.controller.CuponControllerV2;
import com.duoc.Edutech.model.Cupon;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CuponModelAssembler implements RepresentationModelAssembler<Cupon, EntityModel<Cupon>> {

    @Override
    public EntityModel<Cupon> toModel(Cupon cupon) {
        return EntityModel.of(cupon,
                linkTo(methodOn(CuponControllerV2.class).crearCupon(cupon)).withRel("crear cupon nuevo"),
                linkTo(methodOn(CuponControllerV2.class).buscarporid(cupon.getIdcupon())).withRel("buscar por id el cupon"),
                linkTo(methodOn(CuponControllerV2.class).deleteById(cupon.getIdcupon())).withRel("borrar cupon"));

    }

}
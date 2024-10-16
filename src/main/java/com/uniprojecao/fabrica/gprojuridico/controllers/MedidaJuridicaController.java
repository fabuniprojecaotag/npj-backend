package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.services.MedidaJuridicaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/medidas juridicas")
public class MedidaJuridicaController extends FirestoreController<MedidaJuridica> {
    protected MedidaJuridicaController(MedidaJuridicaService genericService) {
        super(genericService);
    }
}



package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/atendimentos")
public class AtendimentosController extends FirestoreController<Atendimento>{
    protected AtendimentosController(AtendimentoService genericService) {
        super(genericService);
    }
}

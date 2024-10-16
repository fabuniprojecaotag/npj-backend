package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.services.ProcessoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/processos")
public class ProcessoController extends FirestoreController<Processo> {
    protected ProcessoController(ProcessoService genericService) {
        super(genericService);
    }
}

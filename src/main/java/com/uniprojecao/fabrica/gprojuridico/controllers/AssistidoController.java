package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.AtendimentoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.ProcessoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.PROCESSOS_COLLECTION;

@RestController
@RequestMapping("/api/assistidos")
public class AssistidoController extends FirestoreController<Assistido> {
    private static final String COLLECTION_ASSISTIDOS = "assistidos";
    @PostMapping
    public ResponseEntity<Assistido> insert(@RequestBody Assistido payload) throws Exception {
        return super.insert(COLLECTION_ASSISTIDOS, payload);
    }

    @GetMapping
    public ResponseEntity<ListBodyDTO<Assistido>> findAll(
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        return super.findAll(COLLECTION_ASSISTIDOS, startAfter, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assistido> findById(@PathVariable String id) throws Exception {
        return super.findById(COLLECTION_ASSISTIDOS, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UpdateBodyDTO<Assistido> payload)
            throws Exception {
        return super.update(COLLECTION_ASSISTIDOS, id, payload);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteBodyDTO payload) {
        return super.delete(COLLECTION_ASSISTIDOS, payload);
    }

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<ListBodyDTO<AtendimentoVinculadoDTO>> findAllAtendimentosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("envolvidos.assistido.id", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl<AtendimentoVinculadoDTO>(ATENDIMENTOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}/processos")
    public ResponseEntity<ListBodyDTO<ProcessoVinculadoDTO>> findAllProcessosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("assistidoId", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl<ProcessoVinculadoDTO>(PROCESSOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }
}

package app.web.gprojuridico.controller;

import app.web.gprojuridico.model.Atendimento;
import app.web.gprojuridico.service.AtendimentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @Autowired
    private AtendimentoService service;

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody Atendimento data)  {
        Map<String, Object> result = service.insert(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.get("id")).toUri();
        return ResponseEntity.created(uri).body(result.get("object"));
    }

    @GetMapping
    public List<Object> findAll()  {
        System.out.println("GET Atendimentos chamado!");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable String id)  {
        System.out.println("GET Atendimento by id chamado!");
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody Map<String, Object> data)  {
        System.out.println("PUT Atendimento chamado!");
        service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id)  {
        System.out.println("DELETE Atendimento chamado!");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

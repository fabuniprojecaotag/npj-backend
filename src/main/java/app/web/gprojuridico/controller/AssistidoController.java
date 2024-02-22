package app.web.gprojuridico.controller;

import app.web.gprojuridico.service.AssistidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assistidos")
public class AssistidoController {

    @Autowired
    private AssistidoService service;

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody Map<String, Object> data) {
        service.insert(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll(@RequestParam(defaultValue = "20") String limit) {
        List<Object> list = service.findAll(limit);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(defaultValue = "20") String limit) {
        service.deleteAll(limit);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        Object object = service.findById(id);
        return ResponseEntity.ok(object);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        service.update(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package app.web.gprojuridico.controller;

import app.web.gprojuridico.model.Assistido;
import app.web.gprojuridico.service.AssistidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assistido")
public class AssistidoController {

    @Autowired
    private AssistidoService assistidoService;

    @GetMapping("/todos")
    public ResponseEntity<List<Assistido>> findAll() {
        List<Assistido> assistidoList = assistidoService.findAll();

        return ResponseEntity.ok(assistidoList);
    }

    @GetMapping("/{assistidoId}")
    public ResponseEntity<Assistido> findById(@PathVariable String assistidoId) {
        Assistido assistido = assistidoService.findById(assistidoId);

        if (assistido != null) {
            return ResponseEntity.ok(assistido);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Assistido> insert(@RequestBody Assistido assistido) {
        assistidoService.insert(assistido);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/{assistidoId}")
    public ResponseEntity<Assistido> update(@PathVariable String assistidoId, @RequestBody Assistido assistido) {
        Assistido assistoAtualizado = assistidoService.update(assistidoId, assistido);
        return ResponseEntity.ok(assistoAtualizado);
    }

    @DeleteMapping("/deletar/{assistidoId}")
    public ResponseEntity<?> deleteAssistido(@PathVariable String assistidoId) {
        assistidoService.delete(assistidoId);
        return ResponseEntity.ok().build();
    }
}

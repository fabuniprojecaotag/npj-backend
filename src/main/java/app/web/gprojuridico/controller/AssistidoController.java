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
    private final AssistidoService assistidoService;

    public AssistidoController(AssistidoService assistidoService) {
        this.assistidoService = assistidoService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Assistido>> getAllAssistidos() {
        List<Assistido> assistidoList = assistidoService.getAllAssistidos();

        return ResponseEntity.ok(assistidoList);
    }

    @GetMapping("/get/{assistidoId}")
    public ResponseEntity<Assistido> getAssistidoById(@PathVariable String assistidoId) {
        Assistido assistido = assistidoService.getAssistidoById(assistidoId);

        if (assistido != null) {
            return ResponseEntity.ok(assistido);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Assistido> createAssistido(@RequestBody Assistido assistido) {
        Assistido assistidoCriado = assistidoService.createAssistido(assistido);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{assistidoId}")
    public ResponseEntity<Assistido> updateAssistido(@PathVariable String assistidoId, @RequestBody Assistido assistido) {
        Assistido assistoAtualizado = assistidoService.updateAssistido(assistidoId, assistido);
        return ResponseEntity.ok(assistoAtualizado);
    }

    @DeleteMapping("/deleteUser/{assistidoId}")
    public ResponseEntity deleteAssistido(@PathVariable String assistidoId) {
        assistidoService.deleteAssistido(assistidoId);
        return ResponseEntity.ok().build();
    }
}

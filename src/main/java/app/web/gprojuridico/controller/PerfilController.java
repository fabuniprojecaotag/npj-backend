package app.web.gprojuridico.controller;


import app.web.gprojuridico.model.user.Perfil;
import app.web.gprojuridico.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @GetMapping("/all")
    public ResponseEntity<List<Perfil>> getAllUsers() {
        List<Perfil> listaPerfis = perfilService.getAll();

        return ResponseEntity.ok(listaPerfis);
    }

    @GetMapping("/get/{perfilId}")
    public ResponseEntity<Perfil> getPerfilById(@PathVariable String perfilId) {
        Perfil perfilResponse = perfilService.getPerfilById(perfilId);

        if (perfilResponse != null) {
            return ResponseEntity.ok(perfilResponse);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

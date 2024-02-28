package app.web.gprojuridico.controller;


import app.web.gprojuridico.model.Role;
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
    public ResponseEntity<List<Role>> getAllUsers() {
        List<Role> listaPerfis = perfilService.getAll();

        return ResponseEntity.ok(listaPerfis);
    }

    @GetMapping("/get/{perfilId}")
    public ResponseEntity<Role> getPerfilById(@PathVariable String perfilId) {
        Role roleResponse = perfilService.getPerfilById(perfilId);

        if (roleResponse != null) {
            return ResponseEntity.ok(roleResponse);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

package app.web.gprojuridico.controller;

import app.web.gprojuridico.model.user.AuthenticationDTO;
import app.web.gprojuridico.model.user.LoginResponseDTO;
import app.web.gprojuridico.model.Usuario;
import app.web.gprojuridico.security.TokenService;
import app.web.gprojuridico.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> verifyLogin(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String access_token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(access_token));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> create(@RequestBody @Valid Usuario data) {
        Map<String, Object> result = userService.create(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.get("id")).toUri();
        return ResponseEntity.created(uri).body(result.get("object"));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll(@RequestParam(defaultValue = "20") String limit) {
        List<Object> usuarios = userService.findAll(limit);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        Object usuario = userService.loadUserByUsername(id);
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        userService.update(id, data);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/my-profile")
//    public ResponseEntity<Usuario> obterMeuPerfil() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication.getPrincipal() instanceof Usuario) {
//            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
//            return ResponseEntity.ok(usuarioAutenticado);
//        } else {
//            // Lida com a situação em que o principal não é do tipo User
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//    @DeleteMapping("/deleteUser/{docId}")
//    public ResponseEntity<String> deleteUser(@PathVariable String docId) {
//        userService.deleteUserById(docId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/toggleStatus/{docId}")
//    public ResponseEntity<ArrayList> toggleStatus(@PathVariable String docId) {
//        ArrayList response = userService.toggleUserStatus(docId);
//        return ResponseEntity.ok(response);
//    }
}

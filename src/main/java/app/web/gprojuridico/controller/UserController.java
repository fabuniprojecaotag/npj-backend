package app.web.gprojuridico.controller;

import app.web.gprojuridico.model.user.AuthenticationDTO;
import app.web.gprojuridico.model.user.LoginResponseDTO;
import app.web.gprojuridico.model.user.User;
import app.web.gprojuridico.security.TokenService;
import app.web.gprojuridico.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

        String access_token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(access_token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody @Valid User data) throws ExecutionException, InterruptedException {
        if (this.userService.findUserByEmail(data.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }

        userService.create(data);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        System.out.println("Tamanho da lista de usuários: " + users.size());
        System.out.println("Lista de usuários: " + users);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<User> obterMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            User usuarioAutenticado = (User) authentication.getPrincipal();
            return ResponseEntity.ok(usuarioAutenticado);
        } else {
            // Lida com a situação em que o principal não é do tipo User
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/get/{usuarioId}")
    public ResponseEntity<User> getUserById(@PathVariable String usuarioId) {
        User user = userService.getUserById(usuarioId);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser/{docId}")
    public ResponseEntity<String> deleteUser(@PathVariable String docId) {
        userService.deleteUserById(docId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/toggleStatus/{docId}")
    public ResponseEntity<ArrayList> toggleStatus(@PathVariable String docId) {
        ArrayList response = userService.toggleUserStatus(docId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateUser/{docId}")
    public ResponseEntity<User> editUser(@PathVariable User data) {
        userService.updateUser(data);
        return ResponseEntity.ok().build();
    }
}

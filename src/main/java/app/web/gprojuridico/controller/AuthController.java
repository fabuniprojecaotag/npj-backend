package app.web.gprojuridico.controller;

import app.web.gprojuridico.dto.AuthenticationDTO;
import app.web.gprojuridico.dto.LoginResponseDTO;
import app.web.gprojuridico.model.Usuario;
import app.web.gprojuridico.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> verifyLogin(@RequestBody @Valid AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication auth = authenticationManager.authenticate(u);
        String accessToken = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }
}

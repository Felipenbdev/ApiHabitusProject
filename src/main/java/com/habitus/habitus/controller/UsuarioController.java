package com.habitus.habitus.controller;

import com.habitus.habitus.dto.LoginRequest;
import com.habitus.habitus.dto.LoginResponse;
import com.habitus.habitus.model.Usuario;
import com.habitus.habitus.repository.UsuarioRepository;
import com.habitus.habitus.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             JwtService jwtService,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder; // BCryptPasswordEncoder
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Usuario usuario) {


        if (usuario.getUsername() == null ||
                usuario.getUsername().trim().isEmpty() ||
                usuarioRepository.findByUsername(usuario.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Erro: username inválido ou já existe!");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(salvo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByUsername(request.username());

        if (usuario == null ||
                !passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body("Erro: credenciais inválidas!");
        }

        String token = jwtService.generateToken(usuario.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
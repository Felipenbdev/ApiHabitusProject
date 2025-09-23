package com.habitus.habitus.controller;

import com.habitus.habitus.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.habitus.habitus.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty() ||
                usuarioRepository.findByUsername(usuario.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Erro: username inválido ou já existe!");
        }
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(salvo);
    }


    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String username,
                                         @RequestParam String senha,
                                         HttpSession session) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            return ResponseEntity.status(401).build();
        }
        session.setAttribute("user", usuario);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout feito!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return ResponseEntity.status(401).body("Nenhum usuário logado");
        return ResponseEntity.ok(usuario);
    }
}

package com.habitus.habitus.controller;

import com.habitus.habitus.model.Tarefa;
import com.habitus.habitus.model.Usuario;
import com.habitus.habitus.repository.TarefasRepository;
import com.habitus.habitus.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefasRepository tarefasRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaController(TarefasRepository tarefasRepository,
                            UsuarioRepository usuarioRepository) {
        this.tarefasRepository = tarefasRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username);
    }

    @GetMapping
    public ResponseEntity<?> listar(Authentication authentication) {

        Usuario usuario = getUsuarioAutenticado(authentication);

        return ResponseEntity.ok(
                tarefasRepository.findByUsuarioId(usuario.getId())
        );
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Tarefa tarefa,
                                   Authentication authentication) {

        Usuario usuario = getUsuarioAutenticado(authentication);

        tarefa.setUsuario(usuario);
        tarefa.setAtivo(false);

        return ResponseEntity.ok(tarefasRepository.save(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id,
                                     Authentication authentication) {

        Usuario usuario = getUsuarioAutenticado(authentication);

        Tarefa tarefa = tarefasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403)
                    .body("Essa tarefa não pertence a você");
        }

        tarefasRepository.delete(tarefa);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTarefa(@PathVariable Long id,
                                          Authentication authentication) {

        Usuario usuario = getUsuarioAutenticado(authentication);

        Tarefa tarefa = tarefasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403)
                    .body("Essa tarefa não pertence a você");
        }

        tarefa.setAtivo(!tarefa.getAtivo());
        tarefasRepository.save(tarefa);

        return ResponseEntity.ok(tarefa);
    }
}
package com.habitus.habitus.controller;

import com.habitus.habitus.model.Tarefa;
import com.habitus.habitus.model.Usuario;
import com.habitus.habitus.repository.TarefasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefasRepository tarefasRepository;


    @GetMapping
    public ResponseEntity<?> listar(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return ResponseEntity.status(401).body("Faça login primeiro");

        return ResponseEntity.ok(tarefasRepository.findByUsuarioId(usuario.getId()));
    }


    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Tarefa tarefa, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return ResponseEntity.status(401).body("Faça login primeiro");

        tarefa.setUsuario(usuario);
        return ResponseEntity.ok(tarefasRepository.save(tarefa));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return ResponseEntity.status(401).body("Faça login primeiro");

        Tarefa tarefa = tarefasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body("Essa tarefa não pertence a você");
        }

        tarefasRepository.delete(tarefa);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTarefa(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return ResponseEntity.status(401).body("Faça login primeiro");

        Tarefa tarefa = tarefasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body("Essa tarefa não pertence a você");
        }

        // Inverte o estado de ativo
        tarefa.setAtivo(!tarefa.getAtivo());
        tarefasRepository.save(tarefa);

        return ResponseEntity.ok(tarefa);
    }

}

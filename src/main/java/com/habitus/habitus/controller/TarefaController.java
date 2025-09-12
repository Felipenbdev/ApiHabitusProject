package com.habitus.habitus.controller;

import com.habitus.habitus.model.Tarefa;
import com.habitus.habitus.repository.TarefasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefasRepository tarefasRepository;


    @GetMapping
    public List<Tarefa> listar() {
        return tarefasRepository.findAll();
    }


    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) {
        return tarefasRepository.findById(id).orElse(null);
    }


    @PostMapping
    public Tarefa criar(@RequestBody Tarefa tarefa) {
        return tarefasRepository.save(tarefa);
    }


    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        tarefasRepository.deleteById(id);
    }


    @GetMapping("/usuario/{usuarioId}")
    public List<Tarefa> listarPorUsuario(@PathVariable Long usuarioId) {
        return tarefasRepository.findByUsuarioId(usuarioId);
    }


    @DeleteMapping("/usuario/{usuarioId}")
    public void deletarTarefasDoUsuario(@PathVariable Long usuarioId) {
        List<Tarefa> tarefas = tarefasRepository.findByUsuarioId(usuarioId);
        tarefasRepository.deleteAll(tarefas);
    }


    @DeleteMapping("/usuario/{usuarioId}/tarefa/{tarefaId}")
    public void deletarTarefaDoUsuario(@PathVariable Long usuarioId, @PathVariable Long tarefaId) {
        Tarefa tarefa = tarefasRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!tarefa.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Essa tarefa não pertence a este usuário");
        }

        tarefasRepository.delete(tarefa);
    }

}

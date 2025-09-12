package com.habitus.habitus.repository;

import com.habitus.habitus.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefasRepository extends JpaRepository<Tarefa,Long> {

    List<Tarefa> findByUsuarioId(Long usuarioId);
}

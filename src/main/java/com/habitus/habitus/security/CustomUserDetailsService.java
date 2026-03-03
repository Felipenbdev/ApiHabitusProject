package com.habitus.habitus.security;

import com.habitus.habitus.model.Usuario;
import com.habitus.habitus.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) throw new UsernameNotFoundException("Usuário não encontrado");

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}

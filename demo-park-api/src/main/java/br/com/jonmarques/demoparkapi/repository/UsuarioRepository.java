package br.com.jonmarques.demoparkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jonmarques.demoparkapi.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

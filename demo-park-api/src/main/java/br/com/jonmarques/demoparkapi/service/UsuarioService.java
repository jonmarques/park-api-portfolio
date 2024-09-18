package br.com.jonmarques.demoparkapi.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jonmarques.demoparkapi.entity.Usuario;
import br.com.jonmarques.demoparkapi.exception.EntityNotFoundException;
import br.com.jonmarques.demoparkapi.exception.PasswordInvalidException;
import br.com.jonmarques.demoparkapi.exception.UsernameUniqueViolationException;
import br.com.jonmarques.demoparkapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		try {
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException ex) {
			throw new UsernameUniqueViolationException(String.format("Username {%s} já cadastrado", usuario.getUsername()));
		}
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id))
		);
	}

	@Transactional
	public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
		if(!novaSenha.equals(confirmaSenha)) {
			throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
		}
		
		Usuario user = buscarPorId(id);
		
		if(!user.getPassword().equals(senhaAtual)) {
			throw new PasswordInvalidException("Sua senha não confere.");
		}
		
		user.setPassword(novaSenha);
		return user;
	}

	@Transactional(readOnly = true)
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
	
}

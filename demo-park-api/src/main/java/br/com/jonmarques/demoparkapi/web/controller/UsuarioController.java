package br.com.jonmarques.demoparkapi.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jonmarques.demoparkapi.entity.Usuario;
import br.com.jonmarques.demoparkapi.service.UsuarioService;
import br.com.jonmarques.demoparkapi.web.dto.UsuarioCreateDto;
import br.com.jonmarques.demoparkapi.web.dto.UsuarioResponseDto;
import br.com.jonmarques.demoparkapi.web.dto.UsuarioSenhaDto;
import br.com.jonmarques.demoparkapi.web.dto.mapper.UsuarioMapper;
import br.com.jonmarques.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name="Usuários", description="Operações relacionados aos usuários da aplicação")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;
	
	@Operation(summary = "Criar um novo usuário", description="Recurso para criar um novo usuário", responses= {
			@ApiResponse(responseCode="201", description="Recurso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode="409", description="Usuário e-mail já cadastrado no sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode="422", description="Recurso não processado por dados de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
	})
	@PostMapping
	public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
		Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
	}
	
	@Operation(summary = "Recuperar um usuário pelo ID", description="Recuperar um usuário pelo ID", responses= {
			@ApiResponse(responseCode="200", description="Recurso recuperado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode="404", description="Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
	})
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
		Usuario user = usuarioService.buscarPorId(id);	
		return ResponseEntity.ok(UsuarioMapper.toDto(user));
	}
	
	@Operation(summary = "Atualizar senha", description="Atualizar senha", responses= {
			@ApiResponse(responseCode="204", description="Senha atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode="400", description="Senha não confere", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			@ApiResponse(responseCode="404", description="Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
	})
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
		usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Recuperar todos os usuários", description="Recuperar todos os usuários", responses= {
			@ApiResponse(responseCode="200", description="Usuários recuperados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))
	})
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDto>> getAll() {
		List<Usuario> user = usuarioService.buscarTodos();
		return ResponseEntity.ok(UsuarioMapper.toListDto(user));
	}
}
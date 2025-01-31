package br.com.jonmarques.demoparkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.jonmarques.demoparkapi.web.dto.UsuarioCreateDto;
import br.com.jonmarques.demoparkapi.web.dto.UsuarioResponseDto;
import br.com.jonmarques.demoparkapi.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

	@Autowired
	WebTestClient testClient;
	
	@Test
	public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
	UsuarioResponseDto responseBody = testClient
		.post()
		.uri("/api/v1/usuarios")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(new UsuarioCreateDto("tody@email.com", "123456"))
		.exchange()
		.expectStatus().isCreated()
		.expectBody(UsuarioResponseDto.class)
		.returnResult().getResponseBody();
	
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
		org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

	}	
	
	@Test
	public void createUsuario_ComUsernameInvalido_RetornarErrorMessageComStatus422() {
	ErrorMessage responseBody = testClient
		.post()
		.uri("/api/v1/usuarios")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(new UsuarioCreateDto("", "123456"))
		.exchange()
		.expectStatus().isEqualTo(422)
		.expectBody(ErrorMessage.class)
		.returnResult().getResponseBody();
	
		org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
		org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient
				.post()
				.uri("/api/v1/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioCreateDto("tody@", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();
			
				org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
				org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

				responseBody = testClient
						.post()
						.uri("/api/v1/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(new UsuarioCreateDto("tody@email", "123456"))
						.exchange()
						.expectStatus().isEqualTo(422)
						.expectBody(ErrorMessage.class)
						.returnResult().getResponseBody();
					
						org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
						org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
	}
	
	
}

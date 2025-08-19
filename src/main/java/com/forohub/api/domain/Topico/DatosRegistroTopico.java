package com.forohub.api.domain.Topico;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull @JsonAlias("usuario_id") Long usuarioId,
        @NotNull Curso curso
) {
}

package com.forohub.api.domain.Topico;

import jakarta.validation.constraints.NotNull;

public record putTopico(
        @NotNull Long id,
        String titulo,
        String mensaje,
        Curso curso
) {
}

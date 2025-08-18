package com.forohub.api.domain.Topico;

import java.time.LocalDateTime;

public record getTopico(
        String titulo,
        String mensaje,
        LocalDateTime fechaDeCreacion,
        Curso curso

        ) {
    public getTopico(Topico topico) {
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getCurso()
                );
    }
}

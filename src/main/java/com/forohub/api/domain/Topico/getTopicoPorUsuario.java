package com.forohub.api.domain.Topico;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record getTopicoPorUsuario(
        String titulo,
        String mensaje,
        @JsonFormat(pattern = "dd/MMM/yyyy HH:mm")
        LocalDateTime fechaDeCreacion,
        Curso curso

) {
    public getTopicoPorUsuario(Topico topico) {
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getCurso()
        );
    }
}

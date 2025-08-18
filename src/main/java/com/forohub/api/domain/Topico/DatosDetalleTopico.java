package com.forohub.api.domain.Topico;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record DatosDetalleTopico(
    Long id,
    String titulo,
    String mensaje,
    @JsonFormat(pattern = "dd/MMM/yyyy HH:mm")
    LocalDateTime fechaDeCreacion,
    boolean status,
    String autor,
    Curso curso
) {
    public DatosDetalleTopico(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.isStatus(),
                topico.getUsuario().getNombre(),
                topico.getCurso()
        );
    }
}

package com.forohub.api.domain.Topico;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record DatosDetalleTopico(
    Long id,
    String titulo,
    String mensaje,
    Curso curso,
    String autor,
    @JsonFormat(pattern = "dd/MMM/yyyy HH:mm")
    LocalDateTime fechaDeCreacion,
    boolean status


) {
    public DatosDetalleTopico(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getCurso(),
                topico.getUsuario().getNombre(),
                topico.getFechaDeCreacion(),
                topico.isStatus()


        );
    }
}

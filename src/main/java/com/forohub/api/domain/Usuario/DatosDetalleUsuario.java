package com.forohub.api.domain.Usuario;

public record DatosDetalleUsuario(Long id, String email, String nombre) {
    public DatosDetalleUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre()
        );
    }
}

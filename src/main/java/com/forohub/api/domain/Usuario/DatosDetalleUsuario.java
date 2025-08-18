package com.forohub.api.domain.Usuario;

public record DatosDetalleUsuario(Long id, String email, String nombre, boolean activo) {
    public DatosDetalleUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.isActivo()
        );
    }
}

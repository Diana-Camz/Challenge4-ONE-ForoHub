package com.forohub.api.domain.Usuario;

public record getUsuario(
        String nombre,
        String email

) {
    public getUsuario(Usuario usuario){
        this(
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}

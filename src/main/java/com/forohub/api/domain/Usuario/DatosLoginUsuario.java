package com.forohub.api.domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosLoginUsuario(
        @NotBlank @Email String email,
        @NotBlank String contrasena) {
}

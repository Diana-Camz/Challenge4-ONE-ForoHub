package com.forohub.api.controllers;

import com.forohub.api.domain.Usuario.DatosLoginUsuario;
import com.forohub.api.domain.Usuario.Usuario;
import com.forohub.api.infra.security.DatosTokenJWT;
import com.forohub.api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Inicio de Sesion", description = "Necesario para realizar peticiones")
@RestController
@RequestMapping("/login")
public class AutenticacionController {

    private final TokenService tokenService;
    private final AuthenticationProvider authenticationProvider;

    public AutenticacionController(TokenService tokenService,
                                   AuthenticationProvider authenticationProvider) {
        this.tokenService = tokenService;
        this.authenticationProvider = authenticationProvider;
    }

    @Operation(summary = "PERMITE INICIAR SESION PARA PODER REALIZAR PETICIONES", description = "Devuelve un token JWT que sera usado para realizar las peticiones autorizadas del rol")
    @ApiResponse(responseCode = "200", description = "Inicio de sesion satisfactorio")
    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosLoginUsuario datos){
        var tokenAutenticacion = new UsernamePasswordAuthenticationToken(datos.email(), datos.contrasena());
        var autenticacion = authenticationProvider.authenticate(tokenAutenticacion);
        var user = (UserDetails) autenticacion.getPrincipal();
        var tokenJWT = tokenService.generarToken(user.getUsername());
        //var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}

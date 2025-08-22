package com.forohub.api.controllers;

import com.forohub.api.domain.Usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Usuarios", description = "CRUD de usuarios del foro")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;
    private PasswordEncoder contrasena;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder contrasena){
        this.usuarioRepository = repository;
        this.contrasena = contrasena;
    }

    //CREAR UN NUEVO USUARIO CON CONTRASENA HASHEADA Y TOKEN JWT
    @Operation(summary = "CREA UN NUEVO USUARIO CON CONTRASENA HASHEADA", description = "Devuelve el detalle del usuario creado")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @Transactional
    @PostMapping
    public ResponseEntity registroUsuario(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentBuilder){
        var contrasenaEncriptada = contrasena.encode(datos.contrasena());
        var usuario = new Usuario(datos);
        usuario.setContrasena(contrasenaEncriptada);
        usuarioRepository.save(usuario);
        var uri = uriComponentBuilder.path("usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleUsuario(usuario));
    }

    //OBTENER TODOS LOS USUARIOS (ACTIVOS)
    @Operation(summary = "LISTA DE TODOS LOS USUARIOS CREADOS ACTIVOS", description = "Devuelve la lista paginada de usuarios activos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity <Page<getUsuario>>listar(@ParameterObject  @PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion){
        var page = usuarioRepository.findAllByActivoTrue(paginacion).map(getUsuario::new);
        return ResponseEntity.ok(page);
    }


    //ELIMINAR UN USUARIO POR EXCLUSION LOGICA
    @Operation(summary = "ELIMINA UN USUARIO DE MANERA LOGICA (EXCLUSIVO ROLE_ADMIN)", description = "Modifica es estado activo del Usario a false")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var usuario = usuarioRepository.getReferenceById(id);
        usuario.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

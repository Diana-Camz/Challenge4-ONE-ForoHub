package com.forohub.api.controllers;

import com.forohub.api.domain.Usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    @GetMapping
    public ResponseEntity <Page<getUsuario>>listar(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion){
        var page = usuarioRepository.findAllByActivoTrue(paginacion).map(getUsuario::new);
        return ResponseEntity.ok(page);
    }


    //ELIMINAR UN USUARIO POR EXCLUSION LOGICA
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var usuario = usuarioRepository.getReferenceById(id);
        usuario.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

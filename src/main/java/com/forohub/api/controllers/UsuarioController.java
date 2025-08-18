package com.forohub.api.controllers;

import com.forohub.api.domain.Usuario.DatosDetalleUsuario;
import com.forohub.api.domain.Usuario.DatosRegistroUsuario;
import com.forohub.api.domain.Usuario.Usuario;
import com.forohub.api.domain.Usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Transactional
    @PostMapping
    //CREAR UN NUEVO TOPICO
    public ResponseEntity registroUsuario(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentBuilder){
        var usuario = new Usuario(datos);
        usuarioRepository.save(usuario);
        var uri = uriComponentBuilder.path("usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleUsuario(usuario));
    }
}

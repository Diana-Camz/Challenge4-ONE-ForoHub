package com.forohub.api.controllers;


import com.forohub.api.domain.Topico.*;
import com.forohub.api.domain.Usuario.Usuario;
import com.forohub.api.domain.Usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    @Transactional
    @PostMapping
    //CREAR UN NUEVO TOPICO
    public ResponseEntity registroTopico(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentBuilder){
        var usuarioExiste = usuarioRepository.findById(datos.usuarioId());
        if (usuarioExiste.isEmpty()){
            return ResponseEntity.badRequest().body("El usuario no existe");
        }
        var usuario = usuarioExiste.get();
        var topico = new Topico(datos, usuario);
        topicoRepository.save(topico);
        var uri = uriComponentBuilder.path("topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));

    }

    //OBTENER TODOS LOS TOPICOS POR TITULO
    @GetMapping
    public ResponseEntity<Page<getTopico>> listar(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacion){
        var page = topicoRepository.findAllByStatusTrue(paginacion).map(getTopico::new);
        return ResponseEntity.ok(page);
    }

    //OBTENER TODOS LOS TOPICOS POR FECHA DE CREACION
    @GetMapping("/fechaDeCreacion")
    public ResponseEntity<Page<getTopico>>listarPorFecha(@PageableDefault(size = 10, sort = {"fechaDeCreacion"}) Pageable paginacionPorFecha){
        var page = topicoRepository.findAllByStatusTrue(paginacionPorFecha).map(getTopico::new);
        return ResponseEntity.ok(page);
    }

    //OBTENER TODOS LOS TOPICOS DE UN USUARIO
    @GetMapping("/usuario/{id}")
    public ResponseEntity <Page<getTopicoPorUsuario>> listarPorUsuario(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = {"fechaDeCreacion"})
            Pageable paginacionPorUsuario){
        var page = topicoRepository.findAllByUsuarioId(id, paginacionPorUsuario).map(getTopicoPorUsuario::new);
        return ResponseEntity.ok(page);
    }


    //OBTENER UN SOLO TOPICO
    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    //ACTUALIZAR UN TOPICO
    @Transactional
    @PutMapping
    public ResponseEntity actualizar(@RequestBody @Valid putTopico datos){
        if (!topicoRepository.existsById(datos.id())){
            return ResponseEntity.badRequest().body("El topico que se pretende actualizar no existe");
        }
        var topico = topicoRepository.getReferenceById(datos.id());
        topico.actualizarTopico(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    //ELIMINAR UN TOPICO POR EXCLUSION LOGICA
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);
        topico.actualizarStatus(id);
        return ResponseEntity.noContent().build();
    }

    //ELIMINAR UN TOPICO POR EXCLUSION FISICA
    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity eliminarDeBD(@PathVariable Long id){
        if(!topicoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El topico que se pretende elimnar no existe");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

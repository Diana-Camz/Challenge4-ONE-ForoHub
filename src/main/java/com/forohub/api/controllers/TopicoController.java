package com.forohub.api.controllers;


import com.forohub.api.domain.Topico.*;
import com.forohub.api.domain.Usuario.UsuarioRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
@Tag(name = "Tópicos", description = "CRUD de tópicos del foro")
@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    //CREAR UN NUEVO TOPICO
    @Operation(summary = "CREA UN NUEVO TOPICO", description = "Devuelve el detalle del topico creado")
    @ApiResponse(responseCode = "201", description = "Topico creado correctamente")
    @Transactional
    @PostMapping
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
    @Operation(summary = "LISTA DE TODOS LOS TOPICOS CREADOS", description = "Devuelve la lista paginada de tópicos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<Page<getTopico>> listar(@ParameterObject @PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacion){
        var page = topicoRepository.findAllByStatusTrue(paginacion).map(getTopico::new);
        return ResponseEntity.ok(page);
    }

    //OBTENER TODOS LOS TOPICOS POR FECHA DE CREACION
    @Operation(summary = "LISTA DE TODOS LOS TOPICOS CREADOS ORDENADOS POR FECHA DE CREACION", description = "Devuelve la lista paginada de tópicos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/fechaDeCreacion")
    public ResponseEntity<Page<getTopico>>listarPorFecha(@ParameterObject @PageableDefault(size = 10, sort = {"fechaDeCreacion"}) Pageable paginacionPorFecha){
        var page = topicoRepository.findAllByStatusTrue(paginacionPorFecha).map(getTopico::new);
        return ResponseEntity.ok(page);
    }

    //OBTENER TODOS LOS TOPICOS DE UN USUARIO
    @Operation(summary = "LISTA DE TODOS LOS TOPICOS CREADOS POR UN USUARIO", description = "Devuelve la lista paginada de tópicos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/usuario/{id}")
    public ResponseEntity <Page<getTopicoPorUsuario>> listarPorUsuario(
            @ParameterObject
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = {"fechaDeCreacion"})
            Pageable paginacionPorUsuario){
        var page = topicoRepository.findAllByUsuarioId(id, paginacionPorUsuario).map(getTopicoPorUsuario::new);
        return ResponseEntity.ok(page);
    }


    //OBTENER UN SOLO TOPICO
    @Operation(summary = "TOPICO AL ESPECIFICAR SU ID", description = "Devuelve un tópico especifico")
    @ApiResponse(responseCode = "200", description = "Topico obtenido correctamente")
    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    //ACTUALIZAR UN TOPICO
    @Operation(summary = "ACTUALIZAR TOPICO", description = "Devuelve el tópico actualizado")
    @ApiResponse(responseCode = "200", description = "Topico actualizado correctamente")
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
    @Operation(summary = "ELIMINAR TOPICO POR EXCLUSION LOGICA", description = "Modifica el status del Topico a false")
    @ApiResponse(responseCode = "204", description = "Topico eliminado correctamente")
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);
        topico.actualizarStatus(id);
        return ResponseEntity.noContent().build();
    }

    //ELIMINAR UN TOPICO POR EXCLUSION FISICA
    @Operation(summary = "ELIMINAR TOPICO POR EXCLUSION FISICA (EXLUSIVO PARA ROLE_ADMIN)", description = "Elimina definitivamente de la Base de Datos")
    @ApiResponse(responseCode = "204", description = "Topico eliminado correctamente de la base de datos")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity eliminarDeBD(@PathVariable Long id){
        if(!topicoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El topico que se pretende elimnar no existe");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

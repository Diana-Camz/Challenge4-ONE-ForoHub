package com.forohub.api.controllers;


import com.forohub.api.domain.Topico.*;
import com.forohub.api.domain.Usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository topicoRepository;


    @Transactional
    @PostMapping
    //CREAR UN NUEVO TOPICO
    public ResponseEntity registroTopico(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentBuilder){

        var topico = new Topico(datos);
        topicoRepository.save(topico);
        var uri = uriComponentBuilder.path("topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
    }

    //OBTENER TODOS LOS TOPICOS
    /*
    @GetMapping
    public ResponseEntity<Page<getTopico>> listar(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacion){
        var page = topicoRepository.findAllByActivoTrue(paginacion).map(getTopico::new);
        return ResponseEntity.ok(page);

    }
    //OBTENER UN SOLO TOPICO
    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

     */
}

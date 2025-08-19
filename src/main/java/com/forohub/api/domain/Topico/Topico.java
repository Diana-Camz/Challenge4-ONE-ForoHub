package com.forohub.api.domain.Topico;


import com.forohub.api.domain.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Column(unique = true)
    private String mensaje;
    private LocalDateTime fechaDeCreacion;
    private boolean status;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Enumerated(EnumType.STRING)
    private Curso curso;

   public Topico(DatosRegistroTopico topico, Usuario usuario){
       this.titulo = topico.titulo();
       this.mensaje = topico.mensaje();
       this.fechaDeCreacion = LocalDateTime.now();
       this.status = true;
       this.curso = topico.curso();
       this.usuario = usuario;
   }

    public void actualizarTopico(@Valid putTopico datos){
       if(datos.titulo() != null){
           this.titulo = datos.titulo();
       }

       if(datos.mensaje() != null){
           this.mensaje = datos.mensaje();
       }

       if(datos.curso() != null){
           this.curso = datos.curso();
       }
    }

    public void actualizarStatus(Long id) {
        this.status = false;
    }
}

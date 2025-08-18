package com.forohub.api.domain.Usuario;

import com.forohub.api.domain.Topico.Curso;
import com.forohub.api.domain.Topico.DatosRegistroTopico;
import com.forohub.api.domain.Topico.Topico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nombre;
        @Column(unique = true)
        private String email;
        private String contrasena;
        private boolean activo;
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Topico> topicos = new ArrayList<>();

        public Usuario(DatosRegistroUsuario usuario){
            this.nombre = usuario.nombre();
            this.email = usuario.email();
            this.activo = true;
        }

        public void eliminar(Long id) {
        this.activo = false;
    }
}

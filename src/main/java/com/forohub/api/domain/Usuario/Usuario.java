package com.forohub.api.domain.Usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forohub.api.domain.Topico.Topico;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@lombok.ToString(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nombre;
        @Column(unique = true)
        private String email;
        private String contrasena;
        @Enumerated(EnumType.STRING)
        private Rol rol;
        private boolean activo;
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnore
        @ToString.Exclude
        private List<Topico> topicos = new ArrayList<>();

        public Usuario(DatosRegistroUsuario usuario){
            this.nombre = usuario.nombre();
            this.email = usuario.email();
            this.activo = true;
            this.rol = usuario.rol();
        }

    public Usuario(String email, String contrasena){
        this.email = email;
        this.contrasena = contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void eliminar(Long id) {
        this.activo = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activo;
    }
}

package com.forohub.api.infra.security;

import com.forohub.api.domain.Usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null && SecurityContextHolder.getContext().getAuthentication() == null){
           try {
               var subject = tokenService.getSubject(tokenJWT);
               usuarioRepository.findByEmail(subject)
                       .map(u -> new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities()))
                       .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

               //SecurityContextHolder.getContext().setAuthentication(auth);
           }catch(RuntimeException e){

            }
        }
        filterChain.doFilter(request, response);

    }

    private String recuperarToken(HttpServletRequest request) {
        String autorizationHeader = request.getHeader("Authorization");

        if (autorizationHeader != null){
            return autorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
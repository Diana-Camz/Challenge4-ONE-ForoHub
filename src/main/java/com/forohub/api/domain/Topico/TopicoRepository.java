package com.forohub.api.domain.Topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Page<Topico> findAllByStatusTrue(Pageable paginacion);
    Page<Topico> findAllByUsuarioId(Long id, Pageable paginacion);
}

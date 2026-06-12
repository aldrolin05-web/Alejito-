package com.lab8.esports.repository;

import com.lab8.esports.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByTag(String tag);
    boolean existsByNombre(String nombre);
    boolean existsByTag(String tag);
    boolean existsByNombreAndIdNot(String nombre, Long id);
    boolean existsByTagAndIdNot(String tag, Long id);
}

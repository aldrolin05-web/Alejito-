package com.example.servidor.repository;

import com.example.servidor.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {}

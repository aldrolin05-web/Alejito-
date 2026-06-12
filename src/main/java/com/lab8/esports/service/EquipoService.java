package com.lab8.esports.service;

import com.lab8.esports.dto.EquipoResponseDTO;
import com.lab8.esports.dto.EquipoUpdateDTO;
import com.lab8.esports.exception.BusinessException;
import com.lab8.esports.model.Equipo;
import com.lab8.esports.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository repo;

    // Pregunta 1: Registrar equipo
    public Equipo registrar(Equipo equipo) {
        if (repo.existsByNombre(equipo.getNombre()))
            throw new BusinessException("Ya existe un equipo con el nombre: " + equipo.getNombre());
        if (repo.existsByTag(equipo.getTag()))
            throw new BusinessException("Ya existe un equipo con el tag: " + equipo.getTag());
        equipo.setEstado(true);
        return repo.save(equipo);
    }

    // Pregunta 2: Listar todos
    public List<EquipoResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Pregunta 2: Buscar por tag
    public EquipoResponseDTO buscarPorTag(String tag) {
        Equipo equipo = repo.findByTag(tag)
                .orElseThrow(() -> new BusinessException("Equipo con tag '" + tag + "' no encontrado"));
        return toDTO(equipo);
    }

    // Pregunta 3: Actualizar parcialmente
    public Equipo actualizar(Long id, EquipoUpdateDTO dto) {
        Equipo equipo = repo.findById(id)
                .orElseThrow(() -> new BusinessException("Equipo con id " + id + " no encontrado"));

        if (dto.getNombre() != null) {
            if (repo.existsByNombreAndIdNot(dto.getNombre(), id))
                throw new BusinessException("Ya existe un equipo con el nombre: " + dto.getNombre());
            equipo.setNombre(dto.getNombre());
        }
        if (dto.getTag() != null) {
            if (repo.existsByTagAndIdNot(dto.getTag(), id))
                throw new BusinessException("Ya existe un equipo con el tag: " + dto.getTag());
            equipo.setTag(dto.getTag());
        }
        if (dto.getCapitan() != null)    equipo.setCapitan(dto.getCapitan());
        if (dto.getCantidadJugadores() != null) equipo.setCantidadJugadores(dto.getCantidadJugadores());
        if (dto.getJuego() != null)      equipo.setJuego(dto.getJuego());
        if (dto.getPais() != null)       equipo.setPais(dto.getPais());
        if (dto.getCorreo() != null)     equipo.setCorreo(dto.getCorreo());
        if (dto.getTelefono() != null)   equipo.setTelefono(dto.getTelefono());

        return repo.save(equipo);
    }

    // Pregunta 4: Desactivar (eliminación lógica)
    public void desactivar(Long id) {
        Equipo equipo = repo.findById(id)
                .orElseThrow(() -> new BusinessException("Equipo con id " + id + " no encontrado"));
        equipo.setEstado(false);
        repo.save(equipo);
    }

    // Mapeo a DTO
    private EquipoResponseDTO toDTO(Equipo e) {
        EquipoResponseDTO dto = new EquipoResponseDTO();
        dto.setNombre(e.getNombre());
        dto.setTag(e.getTag());
        dto.setCapitan(e.getCapitan());
        dto.setJuego(e.getJuego());
        dto.setPais(e.getPais());
        dto.setEstado(e.isEstado() ? "Activo" : "Inactivo");
        return dto;
    }
}

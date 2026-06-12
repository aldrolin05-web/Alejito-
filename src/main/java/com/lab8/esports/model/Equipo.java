package com.lab8.esports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Column(unique = true)
    private String nombre;

    @NotBlank(message = "El tag es obligatorio")
    @Size(min = 2, max = 5, message = "El tag debe tener entre 2 y 5 caracteres")
    @Column(unique = true)
    private String tag;

    @NotBlank(message = "El nombre del capitán es obligatorio")
    private String capitan;

    @Min(value = 5, message = "La cantidad de jugadores debe ser al menos 5")
    @Max(value = 10, message = "La cantidad de jugadores no puede superar 10")
    private int cantidadJugadores;

    @NotBlank(message = "El juego es obligatorio")
    private String juego;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe contener exactamente 9 dígitos")
    private String telefono;

    private boolean estado = true;
}

package com.lab8.esports.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EquipoUpdateDTO {

    private String nombre;

    @Size(min = 2, max = 5, message = "El tag debe tener entre 2 y 5 caracteres")
    private String tag;

    private String capitan;

    @Min(value = 5, message = "La cantidad de jugadores debe ser al menos 5")
    @Max(value = 10, message = "La cantidad de jugadores no puede superar 10")
    private Integer cantidadJugadores;

    private String juego;

    private String pais;

    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe contener exactamente 9 dígitos")
    private String telefono;
}

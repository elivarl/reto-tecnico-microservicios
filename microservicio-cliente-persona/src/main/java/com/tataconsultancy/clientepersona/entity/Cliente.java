package com.tataconsultancy.clientepersona.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente extends Persona {
    //@NotNull
    //@NotEmpty
    private String clienteId;
    @NotNull
    //@NotEmpty
    private String contrasena;
    private boolean estado;


    public Cliente(Long id, String nombre, String genero, int edad, String identificacion, String direccion, String telefono, String clienteId, String contrasena, boolean estado) {
        super(id, nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public Cliente() {
        super(null, null, null, 0, null, null, null);
    }
}

package com.tataconsultancy.clientepersona.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@DiscriminatorValue("cliente")
@Getter
@Setter
public class Cliente extends Persona {
    private String password;
    private boolean estado;

    public Cliente(){
        super(null, null, null, 0, null, null, null);
    }

    public Cliente(Long id, String nombre, String genero, int edad, String identificacion, String direccion, String telefono, String password, boolean estado) {
        super(id, nombre, genero, edad, identificacion, direccion, telefono);
        this.password = password;
        this.estado = estado;
    }
}

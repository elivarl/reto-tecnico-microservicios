package com.tataconsultancy.cuentamovimientos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MovimientoDTO {
    private Long id;
    @NotBlank(message = "El campo fecha no debe estar en blanco")
    private LocalDate fecha;
    @NotBlank(message = "El campo tipo movimiento no debe estar en blanco")
    private String tipoMovimiento;
    @NotBlank(message = "El campo valor no debe estar en blanco")
    private double valor;
    private double saldo;
    @NotBlank(message = "El campo numero de cuenta no debe estar en blanco")
    private String numeroCuenta;
}

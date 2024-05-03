package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.dto.CuentaDTO;

import java.util.List;

public interface CuentaService {

    CuentaDTO crear(CuentaDTO cuentaDTO);
    List<CuentaDTO> listar();
    CuentaDTO obtenerPorId(Long id);
    CuentaDTO actualizar(CuentaDTO cuentaDTO);
    void eliminarPorId(Long id);
    List<CuentaDTO> findByIdentificacionCliente(String identificacionCliente);
}

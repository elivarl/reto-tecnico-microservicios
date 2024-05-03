package com.tataconsultancy.clientepersona.service;

import com.tataconsultancy.clientepersona.dto.ClienteDTO;
import com.tataconsultancy.clientepersona.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    ClienteDTO crear(ClienteDTO clienteDTO);
    List<ClienteDTO> listar();
    ClienteDTO obtenerPorId(Long id);
    ClienteDTO actualizar(ClienteDTO clienteDTO);
    void eliminarPorId(Long id);
}

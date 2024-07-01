package com.tataconsultancy.clientepersona.service;

import com.tataconsultancy.clientepersona.dto.ClienteDTO;
import com.tataconsultancy.clientepersona.entity.Cliente;
import com.tataconsultancy.clientepersona.entity.MensajeError;
import com.tataconsultancy.clientepersona.exception.CedulaInvalidaException;
import com.tataconsultancy.clientepersona.exception.RecursoNoEncontradoException;
import com.tataconsultancy.clientepersona.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private ClienteRepository clienteRepository;
    private ModelMapper modelMapper;
    private ValidarIdentificacionCedula validarIdentificacionCedula;

    @Override
    public ClienteDTO crear(ClienteDTO clienteDTO) {

        if(!validarIdentificacionCedula.validarCedula(clienteDTO.getIdentificacion())){
            throw new CedulaInvalidaException(MensajeError.IDENTIFICACION_NO_VALIDO);
        }

        Cliente cliente = modelMapper.map(clienteDTO,Cliente.class);
        Cliente clienteDB;

        clienteDB = clienteRepository.save(cliente);

        return modelMapper.map(clienteRepository.save(cliente),ClienteDTO.class);
    }

    @Override
    public List<ClienteDTO> listar() {
        return clienteRepository.findAll().stream().map(
                (cliente)-> modelMapper.map(cliente,ClienteDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public ClienteDTO obtenerPorId(Long id) {
        Cliente cliente=clienteRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO)
        );
        return modelMapper.map(cliente,ClienteDTO.class);
    }

    @Override
    public ClienteDTO actualizar(ClienteDTO clienteDTO) {

        Cliente clienteDB= modelMapper.map(obtenerPorId(clienteDTO.getId()),Cliente.class);

        clienteDB.setNombre(clienteDTO.getNombre());
        clienteDB.setGenero(clienteDTO.getGenero());
        clienteDB.setEdad(clienteDTO.getEdad());
        clienteDB.setDireccion(clienteDTO.getDireccion());
        clienteDB.setTelefono(clienteDTO.getTelefono());
        clienteDB.setEstado(clienteDTO.isEstado());
        return modelMapper.map(clienteRepository.save(clienteDB), ClienteDTO.class);
    }

    @Override
    public void eliminarPorId(Long id) {
        clienteRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO)
        );
        clienteRepository.deleteById(id);
    }
}

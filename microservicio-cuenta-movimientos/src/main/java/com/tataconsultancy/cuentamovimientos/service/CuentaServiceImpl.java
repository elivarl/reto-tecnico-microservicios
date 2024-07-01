package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.dto.CuentaDTO;
import com.tataconsultancy.cuentamovimientos.entity.Cuenta;
import com.tataconsultancy.cuentamovimientos.entity.MensajeError;
import com.tataconsultancy.cuentamovimientos.exception.RecursoNoEncontradoException;
import com.tataconsultancy.cuentamovimientos.repository.CuentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CuentaServiceImpl implements CuentaService{
    private CuentaRepository cuentaRepository;
    private ModelMapper modelMapper;

    @Override
    public CuentaDTO crear(CuentaDTO cuentaDTO) {
        Cuenta cuenta= modelMapper.map(cuentaDTO, Cuenta.class);
        return modelMapper.map(cuentaRepository.save(cuenta),CuentaDTO.class);
    }

    @Override
    public List<CuentaDTO> listar() {
        return cuentaRepository.findAll().stream().map(
                (cuenta)-> modelMapper.map(cuenta,CuentaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CuentaDTO obtenerPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO));
        return modelMapper.map(cuenta,CuentaDTO.class);
    }

    @Override
    public CuentaDTO actualizar(CuentaDTO cuentaDTO) {
        CuentaDTO cuentaDTODB= obtenerPorId(cuentaDTO.getId());

        cuentaDTODB.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaDTODB.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuentaDTODB.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaDTODB.setEstado(cuentaDTO.isEstado());

        Cuenta cuenta = modelMapper.map(cuentaDTODB, Cuenta.class);

        return modelMapper.map(cuentaRepository.save(cuenta), CuentaDTO.class);
    }

    @Override
    public void eliminarPorId(Long id) {
        cuentaRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO));
        cuentaRepository.deleteById(id);
    }

    @Override
    public List<CuentaDTO> findByIdentificacionCliente(String identificacionCliente) {
        return cuentaRepository.findByIdentificacionCliente(identificacionCliente).stream().map(
                (cuenta)-> modelMapper.map(cuenta,CuentaDTO.class)).collect(Collectors.toList());
    }
}

package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.dto.MovimientoDTO;
import com.tataconsultancy.cuentamovimientos.entity.MensajeError;
import com.tataconsultancy.cuentamovimientos.entity.Movimiento;
import com.tataconsultancy.cuentamovimientos.exception.RecursoNoEncontradoException;
import com.tataconsultancy.cuentamovimientos.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MovimientoServiceImpl implements MovimientoService {

    private MovimientoRepository movimientoRepository;
    private ModelMapper modelMapper;
    private ValidaRegistroMovimiento actualizaMovimiento;

    @Override
    public MovimientoDTO crear(MovimientoDTO movimientoDTO) {
        Movimiento movimiento = modelMapper.map(movimientoDTO, Movimiento.class);
        return modelMapper.map(movimientoRepository.save(actualizaMovimiento.actualizarSaldoMovimiento(movimiento)), MovimientoDTO.class);
    }

    @Override
    public List<MovimientoDTO> listar() {
        return movimientoRepository.findAll().stream().map(
                (movimiento )-> modelMapper.map(movimiento,MovimientoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public MovimientoDTO obtenerPorId(Long id) {
        Movimiento movimiento= movimientoRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO)
        );
        return modelMapper.map(movimiento,MovimientoDTO.class);
    }

    @Override
    public MovimientoDTO actualizar(MovimientoDTO movimientoDTO) {
        Movimiento movimientoDB = modelMapper.map(obtenerPorId(movimientoDTO.getId()), Movimiento.class);

        movimientoDB.setFecha(movimientoDTO.getFecha());
        movimientoDB.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimientoDB.setValor(movimientoDTO.getValor());
        movimientoDB.setSaldo(movimientoDTO.getSaldo());

        return modelMapper.map(movimientoRepository.save(movimientoDB),MovimientoDTO.class);
    }

    @Override
    public void eliminarPorId(Long id) {
        movimientoRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO)
        );
        movimientoRepository.deleteById(id);
    }
}

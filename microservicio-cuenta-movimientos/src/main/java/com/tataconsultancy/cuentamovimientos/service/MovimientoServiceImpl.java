package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.dto.MovimientoDTO;
import com.tataconsultancy.cuentamovimientos.entity.Cuenta;
import com.tataconsultancy.cuentamovimientos.entity.MensajeError;
import com.tataconsultancy.cuentamovimientos.entity.Movimiento;
import com.tataconsultancy.cuentamovimientos.entity.TipoMovimiento;
import com.tataconsultancy.cuentamovimientos.exception.CuentaNoEncontradaException;
import com.tataconsultancy.cuentamovimientos.exception.RecursoNoEncontradoException;
import com.tataconsultancy.cuentamovimientos.exception.SaldoInsuficienteException;
import com.tataconsultancy.cuentamovimientos.repository.CuentaRepository;
import com.tataconsultancy.cuentamovimientos.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MovimientoServiceImpl implements MovimientoService {

    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;
    private ModelMapper modelMapper;

    @Override
    public MovimientoDTO crear(MovimientoDTO movimientoDTO) {
        Movimiento movimiento = modelMapper.map(movimientoDTO, Movimiento.class);

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimiento.getNumeroCuenta()).orElseThrow(
                () -> new CuentaNoEncontradaException(MensajeError.CUENTA_NO_ENCONTRADA.toString()));
        Optional<Movimiento> optionalMovimiento = movimientoRepository.obtenerUltimoMovimientoPorNumeroCuenta(cuenta.getNumeroCuenta());

        log.info("Cuenta: {}", cuenta);

        //hay movimientos
        if (optionalMovimiento.isPresent()) {
            log.info("Tipo Movimiento: {}", TipoMovimiento.DEPOSITO);
            if (movimiento.getTipoMovimiento().toUpperCase().equals(TipoMovimiento.DEPOSITO.toString())) {
                movimiento.setSaldo(optionalMovimiento.get().getSaldo() + movimiento.getValor());
            }
            if (movimiento.getTipoMovimiento().toUpperCase().equals(TipoMovimiento.RETIRO.toString())) {
                if (optionalMovimiento.get().getSaldo() < movimiento.getValor()) {
                    throw new SaldoInsuficienteException(MensajeError.SALDO_NO_DISPONIBLE.toString());
                } else {
                    if (movimiento.getValor() < 0) {
                        movimiento.setSaldo(optionalMovimiento.get().getSaldo() - (movimiento.getValor() * -1));
                    } else {
                        movimiento.setSaldo(optionalMovimiento.get().getSaldo() - movimiento.getValor());
                    }
                }
            }

        } else {// no hay movimientos
            if (movimiento.getTipoMovimiento().toUpperCase().equals(TipoMovimiento.DEPOSITO.toString())) {
                movimiento.setSaldo(cuenta.getSaldoInicial() + movimiento.getValor());
            } else {
                if (movimiento.getTipoMovimiento().toUpperCase().equals(TipoMovimiento.RETIRO.toString())) {
                    if (cuenta.getSaldoInicial() < movimiento.getValor()) {
                        throw new SaldoInsuficienteException(MensajeError.SALDO_NO_DISPONIBLE.toString());
                    } else {
                        if (movimiento.getValor() < 0) {
                            movimiento.setSaldo(cuenta.getSaldoInicial() - (movimiento.getValor() * -1));
                        } else {
                            movimiento.setSaldo(cuenta.getSaldoInicial() - movimiento.getValor());
                        }
                    }

                }
            }

        }


        return modelMapper.map(movimientoRepository.save(movimiento), MovimientoDTO.class);
    }

    @Override
    public List<MovimientoDTO> listar() {
        return movimientoRepository.findAll().stream().map(
                (movimiento )-> modelMapper.map(movimiento,MovimientoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public MovimientoDTO obtenerPorId(Long id) {
        Movimiento movimiento= movimientoRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO.toString())
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
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO.toString())
        );
        movimientoRepository.deleteById(id);
    }
}

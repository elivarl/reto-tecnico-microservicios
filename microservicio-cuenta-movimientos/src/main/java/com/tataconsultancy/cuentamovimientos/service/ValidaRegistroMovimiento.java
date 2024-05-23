package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.entity.Cuenta;
import com.tataconsultancy.cuentamovimientos.entity.MensajeError;
import com.tataconsultancy.cuentamovimientos.entity.Movimiento;
import com.tataconsultancy.cuentamovimientos.entity.TipoMovimiento;
import com.tataconsultancy.cuentamovimientos.exception.CuentaNoEncontradaException;
import com.tataconsultancy.cuentamovimientos.exception.SaldoInsuficienteException;
import com.tataconsultancy.cuentamovimientos.repository.CuentaRepository;
import com.tataconsultancy.cuentamovimientos.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ValidaRegistroMovimiento {
    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;

    public Movimiento actualizarSaldoMovimiento(Movimiento movimiento){
        Cuenta cuenta = findByNumeroCuenta(movimiento.getNumeroCuenta());
        log.info("Cuenta: {}", cuenta);
        Optional<Movimiento> optionalMovimiento = movimientoRepository.obtenerUltimoMovimientoPorNumeroCuenta(movimiento.getNumeroCuenta());


        //hay movimientos
        if (optionalMovimiento.isPresent()) {
            log.info("Tipo Movimiento: {}", TipoMovimiento.DEPOSITO);
            if (validarTipoMovimientoDeposito(movimiento.getTipoMovimiento())) {
                movimiento.setSaldo(optionalMovimiento.get().getSaldo() + movimiento.getValor());
            }
            if (validarTipoMovimientoRetiro(movimiento.getTipoMovimiento())) {
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
            if (validarTipoMovimientoDeposito(movimiento.getTipoMovimiento())) {
                movimiento.setSaldo(cuenta.getSaldoInicial() + movimiento.getValor());
            } else {
                if (validarTipoMovimientoRetiro(movimiento.getTipoMovimiento())) {
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
        return movimiento;
    }

    public Optional<Movimiento> obtenerUltimoMovimientoPorNumeroCuenta(String numeroCuenta){
        return movimientoRepository.obtenerUltimoMovimientoPorNumeroCuenta(numeroCuenta);

    }

    public Cuenta findByNumeroCuenta(String numeroCuenta){
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElseThrow(
                () -> new CuentaNoEncontradaException(MensajeError.CUENTA_NO_ENCONTRADA.toString()));
    }

    public boolean validarTipoMovimientoRetiro(String tipoMovimiento){
        return tipoMovimiento.toUpperCase().equals(TipoMovimiento.RETIRO.toString());
    }

    public boolean validarTipoMovimientoDeposito(String tipoMovimiento){
        return tipoMovimiento.toUpperCase().equals(TipoMovimiento.DEPOSITO.toString());
    }
}

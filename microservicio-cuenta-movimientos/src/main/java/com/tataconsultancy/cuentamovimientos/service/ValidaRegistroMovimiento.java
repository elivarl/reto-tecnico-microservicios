package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.entity.Cuenta;
import com.tataconsultancy.cuentamovimientos.entity.MensajeError;
import com.tataconsultancy.cuentamovimientos.entity.Movimiento;
import com.tataconsultancy.cuentamovimientos.entity.TipoMovimiento;
import com.tataconsultancy.cuentamovimientos.exception.*;
import com.tataconsultancy.cuentamovimientos.repository.CuentaRepository;
import com.tataconsultancy.cuentamovimientos.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ValidaRegistroMovimiento {
    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;


    public Movimiento actualizarSaldoMovimiento(Movimiento movimiento) {
        log.info("TIPO MOVIMIENTO: {}", movimiento.getTipoMovimiento());

        if (movimiento.getTipoMovimiento().equals(TipoMovimiento.DEPOSITO.toString()) || movimiento.getTipoMovimiento().equals(TipoMovimiento.RETIRO.toString())) {

            Cuenta cuenta = findByNumeroCuenta(movimiento.getNumeroCuenta());
            log.info("Cuenta: {}", cuenta);
            Optional<Movimiento> optionalMovimiento = movimientoRepository.obtenerUltimoMovimientoPorNumeroCuenta(movimiento.getNumeroCuenta());


            //hay movimientos
            if (optionalMovimiento.isPresent()) {
                log.info("Tipo Movimiento: {}", TipoMovimiento.DEPOSITO);
                if (validarTipoMovimientoDeposito(movimiento.getTipoMovimiento())) {
                    if (movimiento.getValor() < 1) {
                        throw new MovimientoDepositoNegativoException(MensajeError.VALOR_DEPOSITO_NO_VALIDO);
                    }
                    movimiento.setSaldo(optionalMovimiento.get().getSaldo() + movimiento.getValor());
                }else{
                if (validarTipoMovimientoRetiro(movimiento.getTipoMovimiento())) {
                    if (movimiento.getValor() >= 0) {
                        throw new MovimientoRetiroPositivoException(MensajeError.VALOR_RETIRO_NO_VALIDO);
                    }
                    if (optionalMovimiento.get().getSaldo() < (movimiento.getValor() * -1)) {
                        throw new SaldoInsuficienteException(MensajeError.SALDO_INSUFICIENTE);
                    } else {
                        movimiento.setSaldo(optionalMovimiento.get().getSaldo() - (movimiento.getValor() * -1));
                    }
                }
                }

            } else {// no hay movimientos
                if (validarTipoMovimientoDeposito(movimiento.getTipoMovimiento())) {
                    if (movimiento.getValor() < 1) {
                        throw new MovimientoDepositoNegativoException(MensajeError.VALOR_DEPOSITO_NO_VALIDO);
                    }
                    movimiento.setSaldo(cuenta.getSaldoInicial() + movimiento.getValor());
                } else {
                    if (validarTipoMovimientoRetiro(movimiento.getTipoMovimiento())) {
                        if (movimiento.getValor() >= 0) {
                            throw new MovimientoRetiroPositivoException(MensajeError.VALOR_RETIRO_NO_VALIDO);
                        }
                        if (cuenta.getSaldoInicial() < (movimiento.getValor() * -1)) {
                            throw new SaldoInsuficienteException(MensajeError.SALDO_INSUFICIENTE);
                        } else {
                            movimiento.setSaldo(cuenta.getSaldoInicial() - (movimiento.getValor() * -1));
                        }
                    }
                }
            }

        } else {
            log.info("Error tipo: {}", MensajeError.TIPO_TRANSACCION_NO_VALIDA);
            throw new TipoTransaccionNoValidaException(MensajeError.TIPO_TRANSACCION_NO_VALIDA);
        }
        return movimiento;
    }

    public Optional<Movimiento> obtenerUltimoMovimientoPorNumeroCuenta(String numeroCuenta) {
        return movimientoRepository.obtenerUltimoMovimientoPorNumeroCuenta(numeroCuenta);

    }

    public Cuenta findByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElseThrow(
                () -> new CuentaNoEncontradaException(MensajeError.CUENTA_NO_ENCONTRADA));
    }

    public boolean validarTipoMovimientoRetiro(String tipoMovimiento) {
        return tipoMovimiento.toUpperCase().equals(TipoMovimiento.RETIRO.toString());
    }

    public boolean validarTipoMovimientoDeposito(String tipoMovimiento) {
        return tipoMovimiento.toUpperCase().equals(TipoMovimiento.DEPOSITO.toString());
    }
}

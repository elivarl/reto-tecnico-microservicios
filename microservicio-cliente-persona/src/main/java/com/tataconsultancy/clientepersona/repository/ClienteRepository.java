package com.tataconsultancy.clientepersona.repository;

import com.tataconsultancy.clientepersona.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByIdentificacion(String identifiacion);
}

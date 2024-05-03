package com.tataconsultancy.clientepersona.service;

import com.tataconsultancy.clientepersona.dto.ClienteDTO;
import com.tataconsultancy.clientepersona.entity.Cliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClienteResponseProducerService {
    @Value("${spring.rabbitmq.response.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.response.routingKey}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public ClienteResponseProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void responseCliente(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getGenero(), cliente.getEdad(), cliente.getIdentificacion(),cliente.getDireccion(),cliente.getTelefono(),null, cliente.isEstado());
        log.info(String.format("Cliente enviado %s", clienteDTO));
        rabbitTemplate.convertAndSend(exchange, routingKey, cliente);
    }
}

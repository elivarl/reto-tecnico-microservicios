package com.tataconsultancy.cuentamovimientos.service;

import com.tataconsultancy.cuentamovimientos.dto.ClienteDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Getter
public class ClienteResponseConsumer {
    private CompletableFuture<ClienteDTO> clienteDTOCompletableFuture = new CompletableFuture<>();


    @RabbitListener(queues = "${spring.rabbitmq.response.queue}")
    public void recibirClienteDTO(ClienteDTO clienteDTO) {
        log.info(String.format("Cliente recibido"));
        log.info("Cliente: {}", clienteDTO);

        clienteDTOCompletableFuture.complete(clienteDTO);
        clienteDTOCompletableFuture = new CompletableFuture<>();
    }

    public CompletableFuture<ClienteDTO> obtenerClienteDTO() {
        return clienteDTOCompletableFuture;
    }

}

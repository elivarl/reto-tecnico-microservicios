package com.tataconsultancy.clientepersona.service;

import com.tataconsultancy.clientepersona.entity.Cliente;
import com.tataconsultancy.clientepersona.entity.MensajeError;
import com.tataconsultancy.clientepersona.entity.Persona;
import com.tataconsultancy.clientepersona.exception.RecursoNoEncontradoException;
import com.tataconsultancy.clientepersona.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteRequestConsumerService {
    private ClienteRepository clienteRepository;
    private ClienteResponseProducerService clienteResponseService;


    @RabbitListener(queues = "${spring.rabbitmq.request.queue}")
    public void buscarCliente(String identificacion) {
        Cliente clienteDb= clienteRepository.findByIdentificacion(identificacion) .orElseThrow(
                ()-> new RecursoNoEncontradoException(MensajeError.RECURSO_NO_ENCONTRADO.toString())
        );
        //enviar a la cola cliente response

        clienteResponseService.responseCliente(clienteDb);

        log.info(String.format("Identifiacion: %s recibida", identificacion));
        log.info("Cliente: {}", clienteDb);
    }
}

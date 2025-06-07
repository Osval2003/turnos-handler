package umg.edu.gt.turnos_handler.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TurnoProducer {

    private final RabbitTemplate rabbitTemplate;

    public TurnoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensaje(String cola, String mensaje) {
        rabbitTemplate.convertAndSend(cola, mensaje);
    }
}


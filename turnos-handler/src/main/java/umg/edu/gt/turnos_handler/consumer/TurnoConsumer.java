
package umg.edu.gt.turnos_handler.consumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import umg.edu.gt.turnos_handler.config.RabbitMQConfig;
import umg.edu.gt.turnos_handler.model.Turno;
import umg.edu.gt.turnos_handler.producer.TurnoProducer;
import umg.edu.gt.turnos_handler.repository.TurnoRepository;

@Component
public class TurnoConsumer {

    private final TurnoRepository turnoRepository;
    private final TurnoProducer turnoProducer;
    private final ObjectMapper objectMapper;

    public TurnoConsumer(TurnoRepository turnoRepository, TurnoProducer turnoProducer) {
        this.turnoRepository = turnoRepository;
        this.turnoProducer = turnoProducer;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = RabbitMQConfig.COLA_INSERTAR)
    public void consumirInsertarTurno(String mensajeJson) {
        try {
            Turno turno = objectMapper.readValue(mensajeJson, Turno.class);
            turnoRepository.save(turno);

            // Notifica que se creó el turno
            turnoProducer.enviarMensaje(RabbitMQConfig.COLA_NOTIFICAR,
                    "Turno creado: " + turno.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


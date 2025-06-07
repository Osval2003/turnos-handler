
package umg.edu.gt.turnos_handler.config;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COLA_INSERTAR = "insertar_turnos";
    public static final String COLA_NOTIFICAR = "notificar_turnos";

    @Bean
    public Queue queueInsertar() {
        return new Queue(COLA_INSERTAR);
    }

    @Bean
    public Queue queueNotificar() {
        return new Queue(COLA_NOTIFICAR);
    }
}


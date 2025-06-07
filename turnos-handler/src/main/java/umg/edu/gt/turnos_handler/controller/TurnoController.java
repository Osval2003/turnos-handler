
package umg.edu.gt.turnos_handler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.edu.gt.turnos_handler.config.RabbitMQConfig;
import umg.edu.gt.turnos_handler.model.Turno;
import umg.edu.gt.turnos_handler.producer.TurnoProducer;
import umg.edu.gt.turnos_handler.repository.TurnoRepository;

import java.util.Optional;

@RestController
@RequestMapping("/tarea")
public class TurnoController {

    private final TurnoProducer turnoProducer;
    private final TurnoRepository turnoRepository;
    private final ObjectMapper objectMapper;

    public TurnoController(TurnoProducer turnoProducer, TurnoRepository turnoRepository) {
        this.turnoProducer = turnoProducer;
        this.turnoRepository = turnoRepository;
        this.objectMapper = new ObjectMapper();
    }
    @PostMapping
    public ResponseEntity<String> crearTurno(@RequestBody Turno turno) {
        try {
            String mensajeJson = objectMapper.writeValueAsString(turno);
            turnoProducer.enviarMensaje(RabbitMQConfig.COLA_INSERTAR, mensajeJson);
            return ResponseEntity.accepted().body("Turno enviado a la cola para inserción");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Eliminar turno directamente y notificar
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Integer id) {
        Optional<Turno> turnoOpt = turnoRepository.findById(id);
        if (turnoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        turnoRepository.deleteById(id);

        // Notificar eliminación
        turnoProducer.enviarMensaje(RabbitMQConfig.COLA_NOTIFICAR,
                "Turno eliminado: " + id);

        return ResponseEntity.ok("Turno eliminado y notificación enviada");
    }
}


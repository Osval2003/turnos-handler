package umg.edu.gt.turnos_handler.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umg.edu.gt.turnos_handler.model.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {
}

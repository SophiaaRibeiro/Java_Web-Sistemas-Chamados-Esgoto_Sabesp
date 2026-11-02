
package sistemas.chamados.repository;

import sistemas.chamados.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository
        extends JpaRepository<Chamado, Long> {

}

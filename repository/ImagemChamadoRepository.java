
package sistemas.chamados.repository;

import sistemas.chamados.model.ImagemChamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemChamadoRepository
        extends JpaRepository<ImagemChamado, Long> {
}

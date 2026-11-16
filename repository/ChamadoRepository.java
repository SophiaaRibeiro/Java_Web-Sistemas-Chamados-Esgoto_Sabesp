package sistemas.chamados.repository;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByUsuarioOrderByDataAberturaDesc(Usuario usuario);
    List<Chamado> findAllByOrderByDataAberturaDesc();
}

package sistemas.chamados.service;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.repository.ChamadoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    // LISTAR TODOS
    public List<Chamado> listarTodos() {
        return repository.findAll();
    }

    // BUSCAR POR ID
    public Chamado buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Chamado não encontrado"));
    }

    // CADASTRAR OU EDITAR
    public Chamado salvar(Chamado chamado) {

        if (chamado.getId() != null) {
            Chamado existente = buscarPorId(chamado.getId());

            // Preserva a data original de abertura
            chamado.setDataAbertura(existente.getDataAbertura());
        }

        return repository.save(chamado);
    }

    // EXCLUIR
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Chamado não encontrado");
        }

        repository.deleteById(id);
    }

    // CONTAR TODOS
    public long contarTodos() {
        return repository.count();
    }

    // CONTAR CHAMADOS ABERTOS
    public long contarAbertos() {
        return repository.findAll().stream()
                .filter(c -> "ABERTO".equals(c.getStatus()))
                .count();
    }

    // CONTAR CHAMADOS RESOLVIDOS
    public long contarResolvidos() {
        return repository.findAll().stream()
                .filter(c -> "RESOLVIDO".equals(c.getStatus()))
                .count();
    }

    // PESQUISAR E FILTRAR

    public List<Chamado> filtrar(String busca, String status) {

        return repository.findAll().stream()

                .filter(c -> {

                    if (busca == null || busca.isBlank()) {
                        return true;
                    }

                    String termo = busca.toLowerCase(Locale.ROOT);

                    String endereco = c.getEndereco() == null
                            ? "" : c.getEndereco().toLowerCase(Locale.ROOT);

                    String tipo = c.getTipoProblema() == null
                            ? "" : c.getTipoProblema().toLowerCase(Locale.ROOT);

                    return endereco.contains(termo)
                            || tipo.contains(termo);
                })

                .filter(c ->
                        status == null || status.isBlank()
                                || status.equals(c.getStatus()))

                .toList();
    }

}

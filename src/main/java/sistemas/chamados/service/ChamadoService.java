
package sistemas.chamados.service;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.repository.ChamadoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import sistemas.chamados.model.ImagemChamado;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;
    private final Path pastaImagens =
            Paths.get("uploads", "chamados").toAbsolutePath().normalize();
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


    // CADASTRAR OU EDITAR CHAMADO COM IMAGEM

    public Chamado salvar(
            Chamado chamado,
            List<MultipartFile> imagens
    ) throws IOException {

        // Recupera os dados anteriores quando for edição
        if (chamado.getId() != null) {

            Chamado existente = buscarPorId(chamado.getId());

            // Preserva a data de abertura
            chamado.setDataAbertura(existente.getDataAbertura());

            // Preserva a imagem antiga
            chamado.setCaminhoImagem(existente.getCaminhoImagem());

            // Preserva todas as imagens já cadastradas
            chamado.setImagens(existente.getImagens());

        } else {
            chamado.setStatus("ABERTO");
            chamado.setImagens(new ArrayList<>());
        }

        // Processa cada imagem enviada
        if (imagens != null) {

            for (MultipartFile imagem : imagens) {

                if (imagem == null || imagem.isEmpty()) {
                    continue;
                }

                // Limite de 5 MB por imagem
                if (imagem.getSize() > 5 * 1024 * 1024) {
                    throw new IllegalArgumentException(
                            "Cada imagem deve ter no máximo 5 MB."
                    );
                }

                String tipo = imagem.getContentType();
                String extensao;

                if ("image/jpeg".equals(tipo)) {
                    extensao = ".jpg";
                } else if ("image/png".equals(tipo)) {
                    extensao = ".png";
                } else if ("image/webp".equals(tipo)) {
                    extensao = ".webp";
                } else {
                    throw new IllegalArgumentException(
                            "Formato de imagem não permitido."
                    );
                }

                // Cria a pasta se necessário
                Files.createDirectories(pastaImagens);

                // Gera nome único
                String nomeArquivo = UUID.randomUUID() + extensao;

                Path destino = pastaImagens.resolve(nomeArquivo);

                // Salva o arquivo na pasta
                Files.copy(imagem.getInputStream(), destino);

                // Cria o registro da imagem
                ImagemChamado novaImagem = new ImagemChamado();

                novaImagem.setCaminhoImagem(
                        "uploads/chamados/" + nomeArquivo
                );

                novaImagem.setChamado(chamado);

                // Adiciona à lista do chamado
                chamado.getImagens().add(novaImagem);
            }
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


    public Chamado atualizarStatus(Long id, String status) {

        Chamado chamado = buscarPorId(id);

        if (!List.of(
                "ABERTO",
                "EM ANÁLISE",
                "EM ATENDIMENTO",
                "RESOLVIDO"
        ).contains(status)) {
            throw new IllegalArgumentException("Status inválido.");
        }

        chamado.setStatus(status);

        return repository.save(chamado);
    }
    // LISTAR CHAMADOS DE UM USUÁRIO ESPECÍFICO
    public List<Chamado> listarPorUsuario(sistemas.chamados.model.Usuario usuario) {
        return repository.findByUsuarioOrderByDataAberturaDesc(usuario);
    }

    // ATUALIZAR STATUS E COMENTÁRIO (SABESP)
    public Chamado responderSabesp(Long id, String status, String comentarioSabesp) {
        Chamado chamado = buscarPorId(id);

        if (!List.of("ABERTO", "EM ANÁLISE", "EM ATENDIMENTO", "RESOLVIDO").contains(status)) {
            throw new IllegalArgumentException("Status inválido.");
        }

        chamado.setStatus(status);
        chamado.setComentarioSabesp(comentarioSabesp);

        return repository.save(chamado);
    }

    public long contarEmAndamento() {
        return repository.findAll().stream()
                .filter(c -> "EM ANÁLISE".equals(c.getStatus()) || "EM ATENDIMENTO".equals(c.getStatus()))
                .count();
    }

}

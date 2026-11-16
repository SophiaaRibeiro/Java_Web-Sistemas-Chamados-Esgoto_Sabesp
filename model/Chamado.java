package sistemas.chamados.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Novos campos de endereço detalhado
    @Column(length = 9)
    private String cep;

    @Column(nullable = false, length = 160)
    private String logradouro;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 120)
    private String complemento;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(length = 200)
    private String pontoReferencia;

    // Mantido por compatibilidade caso use em algum lugar, ou pode ser preenchido automaticamente
    @Column
    private String endereco;

    @Column(nullable = false)
    private String tipoProblema;

    @Column(length = 2000)
    private String descricao;

    @Column(length = 500)
    private String caminhoImagem;

    @OneToMany(
            mappedBy = "chamado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ImagemChamado> imagens = new ArrayList<>();

    @Column
    private String status;

    private LocalDateTime dataAbertura;

    @Column
    private LocalDate dataOcorrencia;

    @Column
    private LocalTime horarioOcorrencia;

    // Relacionamento com Usuário correto (sem @Column em cima)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 2000)
    private String comentarioSabesp;

    @Column(length = 20)
    private String telefone;


    // Getters e Setters

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getComentarioSabesp() {
        return comentarioSabesp;
    }

    public void setComentarioSabesp(String comentarioSabesp) {
        this.comentarioSabesp = comentarioSabesp;
    }

    // Construtor vazio exigido pelo JPA
    public Chamado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoProblema() {
        return tipoProblema;
    }

    public void setTipoProblema(String tipoProblema) {
        this.tipoProblema = tipoProblema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public List<ImagemChamado> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemChamado> imagens) {
        this.imagens = imagens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public LocalTime getHorarioOcorrencia() {
        return horarioOcorrencia;
    }

    public void setHorarioOcorrencia(LocalTime horarioOcorrencia) {
        this.horarioOcorrencia = horarioOcorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist
    public void antesDeSalvar() {
        if (dataAbertura == null) {
            dataAbertura = LocalDateTime.now();
        }

        if (status == null || status.isBlank()) {
            status = "ABERTO";
        }

        // Monta o endereço consolidado automaticamente para compatibilidade
        if ((this.endereco == null || this.endereco.isBlank()) && this.logradouro != null) {
            this.endereco = this.logradouro + ", " + this.numero + " - " + this.bairro;
        }
    }
}
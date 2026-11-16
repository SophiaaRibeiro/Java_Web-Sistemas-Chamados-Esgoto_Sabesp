# Portal Saneamento Santos
Sistema web integrado para registro, fiscalização e atendimento de ocorrências e problemas de saneamento básico e esgoto em Santos/SP.

---

## 📌 Sobre o Projeto
O **Portal Saneamento Santos** foi desenvolvido em Java com Spring Boot e Thymeleaf para otimizar a comunicação entre a população, a prestadora de serviços (**Sabesp**) e o órgão fiscalizador (**Prefeitura**). O sistema gerencia todo o ciclo de vida de uma ocorrência, desde o registro com evidências fotográficas até o parecer técnico e a conclusão do atendimento.

---

## 👥 Perfis de Acesso e Funcionalidades

O sistema conta com controle de acesso baseado em perfis (Spring Security):

1. **Cidadão (Usuário Comum - `ROLE_USER`)**
   * Cadastro e login no sistema.
   * Abertura de chamados informando endereço detalhado (CEP, logradouro, número, complemento, bairro, ponto de referência), telefone de contato, tipo de problema, data/horário da ocorrência, descrição e upload de múltiplas fotos.
   * Acompanhamento dos próprios chamados com status coloridos e visualização das respostas técnicas da Sabesp.

2. **Sabesp (`ROLE_SABESP`)**
   * Listagem de todas as ocorrências cadastradas no município.
   * Filtros dinâmicos por status e busca textual (por endereço ou tipo de problema) sem necessidade de clique adicional.
   * Visualização de detalhes, fotos e dados do cidadão.
   * Atualização de status (*Aberto, Em Análise, Em Atendimento, Resolvido*) e envio de parecer técnico/comentário para o cidadão e a prefeitura.

3. **Prefeitura / Fiscalização (`ROLE_PREFEITURA`)**
   * Monitoramento em tempo real do volume de ocorrências (cards com contadores de Total, Abertos/Pendentes, Em Andamento e Concluídos).
   * Filtros avançados e automáticos por status e palavra-chave.
   * Acompanhamento transparente das tratativas e prazos realizados pela Sabesp.

---

## 🛠️ Tecnologias Utilizadas

* **Backend:** Java 17+, Spring Boot, Spring MVC, Spring Data JPA, Spring Security
* **Frontend:** Thymeleaf, Bootstrap 5, HTML5, CSS3, JavaScript
* **Banco de Dados:** SQLite (com persistência local via Hibernate)
* **Gerenciamento de Dependências:** Maven



package sistemas.chamados.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sistemas.chamados.model.Perfil;
import sistemas.chamados.model.Usuario;
import sistemas.chamados.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner carregarUsuariosIniciais(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("sabesp@santos.sp.gov.br").isEmpty()) {
                Usuario sabesp = new Usuario();
                sabesp.setNome("Equipe Técnica Sabesp");
                sabesp.setEmail("sabesp@santos.sp.gov.br");
                sabesp.setSenha(encoder.encode("123456"));
                sabesp.setPerfil(Perfil.SABESP);
                repo.save(sabesp);
            }

            if (repo.findByEmail("prefeitura@santos.sp.gov.br").isEmpty()) {
                Usuario prefeitura = new Usuario();
                prefeitura.setNome("Fiscalização Prefeitura");
                prefeitura.setEmail("prefeitura@santos.sp.gov.br");
                prefeitura.setSenha(encoder.encode("123456"));
                prefeitura.setPerfil(Perfil.PREFEITURA);
                repo.save(prefeitura);
            }
        };
    }
}
package sistemas.chamados.service;
import sistemas.chamados.model.Perfil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sistemas.chamados.model.Usuario;
import sistemas.chamados.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrar(Usuario usuario) {

        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        // O cadastro público nunca cria administradores.
        usuario.setPerfil(Perfil.USER);
        repository.save(usuario);
    }
}
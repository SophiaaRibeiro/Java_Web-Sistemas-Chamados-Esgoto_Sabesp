
package sistemas.chamados.controller;

import sistemas.chamados.model.Usuario;
import sistemas.chamados.model.Chamado;
import sistemas.chamados.repository.UsuarioRepository;
import sistemas.chamados.service.UsuarioService;
import sistemas.chamados.service.ChamadoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoService chamadoService;

    public UsuarioController(
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository,
            ChamadoService chamadoService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.chamadoService = chamadoService;
    }

    // Página inicial do usuário com os chamados dele
    @GetMapping("/usuario/home")
    public String home(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        model.addAttribute("chamados", chamadoService.listarPorUsuario(usuario));
        return "produtos/usuario/home";
    }

    // Visualizar detalhes de um chamado do próprio usuário
    @GetMapping("/usuario/chamados/{id}")
    public String detalhesChamado(@PathVariable Long id, Authentication authentication, Model model) {
        Chamado chamado = chamadoService.buscarPorId(id);

        // Garante que o usuário só veja chamados criados por ele mesmo
        if (!chamado.getUsuario().getEmail().equals(authentication.getName())) {
            return "redirect:/usuario/home";
        }

        model.addAttribute("chamado", chamado);
        return "produtos/usuario/chamado-detalhes";
    }

    // Formulário para abrir um novo chamado
    @GetMapping("/usuario/chamados/novo")
    public String novoChamado(Model model) {

        Chamado chamado = new Chamado();
        chamado.setStatus("ABERTO");

        model.addAttribute("chamado", chamado);
        model.addAttribute("titulo", "Novo chamado");

        return "produtos/usuario/chamados-form";
    }

    // Impede o Spring de tentar converter imagens para a entidade
    @InitBinder("chamado")
    public void configurarBinder(WebDataBinder binder) {
        binder.setDisallowedFields("imagens");
    }

    // Salvar chamado do usuário logado
    @PostMapping("/usuario/chamados/salvar")
    public String salvarChamado(
            @ModelAttribute("chamado") Chamado chamado,
            @RequestParam(value = "imagens", required = false)
            List<MultipartFile> imagens,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String email = authentication.getName();

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Usuário não encontrado."));

            // Associa o chamado à conta que está logada
            chamado.setUsuario(usuario);

            // Todo chamado novo começa como ABERTO
            chamado.setId(null);
            chamado.setStatus("ABERTO");

            chamadoService.salvar(chamado, imagens);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Chamado registrado com sucesso!"
            );

            return "redirect:/usuario/home";

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível registrar o chamado: " + e.getMessage()
            );

            return "redirect:/usuario/chamados/novo";
        }
    }

    // Cadastro de usuário
    @PostMapping("/cadastro")
    public String cadastrar(
            @ModelAttribute Usuario usuario,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.cadastrar(usuario);

            return "redirect:/login?cadastroSucesso";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro", e.getMessage()
            );

            return "redirect:/cadastro";
        }
    }
}

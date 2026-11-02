
package sistemas.chamados.controller;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.service.ChamadoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    // ABRIR A PÁGINA INICIAL
    @GetMapping("/chamados")
    public String listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String status,
            Model model) {

        model.addAttribute("chamados",
                service.filtrar(busca, status));

        model.addAttribute("totalChamados",
                service.contarTodos());

        model.addAttribute("chamadosAbertos",
                service.contarAbertos());

        model.addAttribute("chamadosResolvidos",
                service.contarResolvidos());

        model.addAttribute("busca", busca);
        model.addAttribute("statusSelecionado", status);

        return "produtos/chamados";
    }

    // ABRIR FORMULÁRIO DE CADASTRO
    @GetMapping("/chamados/novo")
    public String novo(Model model) {

        Chamado chamado = new Chamado();
        chamado.setStatus("ABERTO");

        model.addAttribute("chamado", chamado);
        model.addAttribute("titulo", "Novo chamado");

        return "produtos/chamados-form";
    }

    // SALVAR CADASTRO OU EDIÇÃO
    @PostMapping("/chamados/salvar")
    public String salvar(@ModelAttribute Chamado chamado) {

        service.salvar(chamado);

        return "redirect:/chamados";
    }

    // ABRIR FORMULÁRIO DE EDIÇÃO
    @GetMapping("/chamados/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Chamado chamado = service.buscarPorId(id);

        model.addAttribute("chamado", chamado);
        model.addAttribute("titulo", "Editar chamado");

        return "produtos/chamados-form";
    }

    // EXCLUIR CHAMADO
    @GetMapping("/chamados/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        service.excluir(id);

        return "redirect:/chamados";
    }
}

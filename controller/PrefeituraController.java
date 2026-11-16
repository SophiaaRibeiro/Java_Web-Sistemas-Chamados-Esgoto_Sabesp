package sistemas.chamados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sistemas.chamados.service.ChamadoService;

@Controller
@RequestMapping("/prefeitura")
public class PrefeituraController {

    private final ChamadoService chamadoService;

    public PrefeituraController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping("/chamados")
    public String acompanharChamados(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String status,
            Model model) {

        model.addAttribute("chamados", chamadoService.filtrar(busca, status));
        model.addAttribute("totalChamados", chamadoService.contarTodos());
        model.addAttribute("chamadosAbertos", chamadoService.contarAbertos());
        model.addAttribute("chamadosResolvidos", chamadoService.contarResolvidos());
        model.addAttribute("busca", busca);
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("chamadosEmAndamento", chamadoService.contarEmAndamento());

        return "produtos/prefeitura/chamados";
    }

    @GetMapping("/chamados/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("chamado", chamadoService.buscarPorId(id));
        return "produtos/prefeitura/chamado-detalhes";
    }
}
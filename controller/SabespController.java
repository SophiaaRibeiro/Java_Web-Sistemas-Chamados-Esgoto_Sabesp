package sistemas.chamados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sistemas.chamados.service.ChamadoService;

@Controller
@RequestMapping("/sabesp")
public class SabespController {

    private final ChamadoService chamadoService;

    public SabespController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping("/chamados")
    public String listarChamados(
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

        return "produtos/sabesp/chamados";
    }

    @GetMapping("/chamados/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("chamado", chamadoService.buscarPorId(id));
        return "produtos/sabesp/chamado-detalhes";
    }

    @PostMapping("/chamados/{id}/atualizar")
    public String atualizarChamado(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comentarioSabesp,
            RedirectAttributes redirectAttributes) {

        try {
            chamadoService.responderSabesp(id, status, comentarioSabesp);
            redirectAttributes.addFlashAttribute("sucesso", "Chamado #" + id + " atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }

        return "redirect:/sabesp/chamados";
    }


}

package sistemas.chamados.controller;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.service.ChamadoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ChamadoService chamadoService;

    public AdminController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    // Lista todos os chamados para o administrador
    @GetMapping("/chamados")
    public String listarChamados(Model model) {

        model.addAttribute(
                "chamados",
                chamadoService.listarTodos()
        );

        return "produtos/admin/chamados";
    }

    // Visualiza os detalhes de um chamado
    @GetMapping("/chamados/{id}")
    public String detalhes(
            @PathVariable Long id,
            Model model) {

        Chamado chamado = chamadoService.buscarPorId(id);

        model.addAttribute("chamado", chamado);

        return "produtos/admin/chamado-detalhes";
    }

    // Atualiza o status do chamado
    @PostMapping("/chamados/{id}/status")
    public String atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        try {
            chamadoService.atualizarStatus(id, status);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Status atualizado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível atualizar o status."
            );
        }

        return "redirect:/admin/chamados/" + id;
    }
}

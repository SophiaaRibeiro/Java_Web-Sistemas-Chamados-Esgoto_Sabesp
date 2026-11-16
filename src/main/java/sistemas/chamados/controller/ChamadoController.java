package sistemas.chamados.controller;

import sistemas.chamados.model.Chamado;
import sistemas.chamados.service.ChamadoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.WebDataBinder;

import java.util.List;

@Controller
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping("/chamados")
    public String listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String status,
            Model model) {

        model.addAttribute("chamados", service.filtrar(busca, status));
        model.addAttribute("totalChamados", service.contarTodos());
        model.addAttribute("chamadosAbertos", service.contarAbertos());
        model.addAttribute("chamadosResolvidos", service.contarResolvidos());
        model.addAttribute("busca", busca);
        model.addAttribute("statusSelecionado", status);

        return "produtos/chamados";
    }
//
//    @GetMapping("/chamados/novo")
//    public String novo(Model model) {
//        Chamado chamado = new Chamado();
//        chamado.setStatus("ABERTO");
//
//        model.addAttribute("chamado", chamado);
//        model.addAttribute("titulo", "Novo chamado");
//
//        return "produtos/chamados-form";
//    }

    @InitBinder("chamado")
    public void configurarBinder(WebDataBinder binder) {
        binder.setDisallowedFields("imagens");
    }


}
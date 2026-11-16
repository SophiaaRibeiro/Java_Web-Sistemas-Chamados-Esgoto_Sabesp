package sistemas.chamados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class RedirecionamentoController {

    @GetMapping("/redirecionar")
    public String redirecionar(Authentication authentication) {
        var authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/chamados";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SABESP"))) {
            return "redirect:/sabesp/chamados";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PREFEITURA"))) {
            return "redirect:/prefeitura/chamados";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return "redirect:/usuario/home";
        }

        return "redirect:/login?erro";
    }
}

package sistemas.chamados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "produtos/auth/login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "produtos/auth/cadastro";
    }
}

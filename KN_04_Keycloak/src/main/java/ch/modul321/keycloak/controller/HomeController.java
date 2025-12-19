package ch.modul321.keycloak.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getPreferredUsername());
            model.addAttribute("authenticated", true);
        } else {
            model.addAttribute("authenticated", false);
        }
        return "index";
    }

    @GetMapping("/public")
    public String publicPage(Model model) {
        model.addAttribute("message", "This is a public page accessible to everyone!");
        return "public";
    }

    @GetMapping("/protected")
    public String protectedPage(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("username", principal.getPreferredUsername());
        model.addAttribute("email", principal.getEmail());
        model.addAttribute("fullName", principal.getFullName());
        model.addAttribute("idToken", principal.getIdToken().getTokenValue());
        model.addAttribute("claims", principal.getClaims());
        return "protected";
    }
}

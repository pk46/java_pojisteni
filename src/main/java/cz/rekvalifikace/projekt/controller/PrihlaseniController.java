package cz.rekvalifikace.projekt.controller;

import cz.rekvalifikace.projekt.model.Pojistenec;
import cz.rekvalifikace.projekt.repository.PojistenecRepository;
import cz.rekvalifikace.projekt.view.PrihlaseniDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class PrihlaseniController {

    @Autowired
    private PojistenecRepository repository;

    @GetMapping(path = "/prihlaseni")
    public ModelAndView prihlaseni(Map<String, Object> model) {
        model.put("title", "Přihlášení uživatele");
        return new ModelAndView("prihlaseni");
    }

    @PostMapping(path = "/prihlaseni", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public RedirectView prihlasUzivatele(HttpServletRequest request, PrihlaseniDto prihlaseniDto) {
        Pojistenec uzivatel = repository.findByEmail(prihlaseniDto.getEmail());
        if (uzivatel != null && uzivatel.getHesloHash().equals(prihlaseniDto.getHeslo())) {
            request.getSession().setAttribute("prihlasen", true);
            return new RedirectView("/pojistenci/1");
        } else {
            return new RedirectView("/prihlaseni?chyba=true");
        }
    }

    public static void overPrihlaseni(HttpServletRequest request) {
        if (!request.getSession().getAttribute("prihlasen").equals(true)) {
            throw new RuntimeException();
        }
    }

}

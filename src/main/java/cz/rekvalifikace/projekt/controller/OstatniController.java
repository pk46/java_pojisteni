package cz.rekvalifikace.projekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/")
public class OstatniController {

    @GetMapping(path = "/oaplikaci")
    public ModelAndView oAplikaci(Map<String, Object> model) {
        model.put("titulek", "O aplikaci");
        return new ModelAndView("o_aplikaci");
    }

}

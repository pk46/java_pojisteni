package cz.rekvalifikace.projekt.controller;

import cz.rekvalifikace.projekt.model.Pojistenec;
import cz.rekvalifikace.projekt.model.Pojisteni;
import cz.rekvalifikace.projekt.repository.PojistenecRepository;
import cz.rekvalifikace.projekt.repository.PojisteniRepository;
import cz.rekvalifikace.projekt.service.TestDataGenerator;
import cz.rekvalifikace.projekt.view.Link;
import cz.rekvalifikace.projekt.view.PojistenecDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/pojistenci")
public class PojistenecController {

    @Autowired
    private PojistenecRepository repository;
    @Autowired
    private PojisteniRepository pojisteniRepository;
    private static final int POCET_NA_STRANKU = 3;
    private String textNotifikace;

    @GetMapping({"/{page}/{notifikace}", "/{page}"})
    public ModelAndView seznamPojistenych(HttpServletRequest request, Map<String, Object> model, @PathVariable int page,
                                          @PathVariable(required = false) boolean notifikace) {
        long pocetStran = (long)Math.ceil(pocetPojistenych()/(float) POCET_NA_STRANKU);
        List<Link> odkazyNaStranky = LongStream.range(1, pocetStran + 1)
                .boxed()
                .map(number -> new Link("/pojistenci/" + number, "" + number, number == page ? "btn-primary" : "btn-outline-primary", 0))
                .collect(Collectors.toList());
        if (page == 1) {
            odkazyNaStranky.add(0, new Link("/pojistenci/" + Math.max(1, page - 1), "Předchozí", "btn-secondary disabled", -1));
            odkazyNaStranky.add(new Link("/pojistenci/" + Math.min(pocetStran, page + 1), "Následující", "btn-outline-primary", 0));
        } else if (page == pocetStran) {
            odkazyNaStranky.add(new Link("/pojistenci/" + Math.min(pocetStran, page + 1), "Následující", "btn-secondary disabled", -1));
            odkazyNaStranky.add(0, new Link("/pojistenci/" + Math.max(1, page - 1), "Předchozí", "btn-outline-primary", 0));
        } else {
            odkazyNaStranky.add(0, new Link("/pojistenci/" + Math.max(1, page - 1), "Předchozí", "btn-outline-primary", 0));
            odkazyNaStranky.add(new Link("/pojistenci/" + Math.min(pocetStran, page + 1), "Následující", "btn-outline-primary", 0));
        }
        model.put("titulek", "Seznam pojištěných");
        model.put("stranky", odkazyNaStranky);
        model.put("aktualniStranka", page-1);
        model.put("seznamPojistenych", repository.findAll(PageRequest.of(page-1, POCET_NA_STRANKU)));
        model.put("text_notifikace", notifikace? textNotifikace:"");
        model.put("notify", notifikace);
        return new ModelAndView("seznam_pojistenych", model);
    }

    private long pocetPojistenych() {
        return repository.count();
    }

    @GetMapping(path = "/detail/{id}")
    public ModelAndView detailPojistence(Map<String, Object> model, @PathVariable Long id) {
        Pojistenec pojistenec = repository.findById(id).get();
        model.put("titulek", "Detail pojištěnce");
        model.put("pojistenec", pojistenec);
        Collection<Pojisteni> pojisteni = pojisteniRepository.findByPojistenecId(id);
        model.put("pojisteni", pojisteni);
        return new ModelAndView("detail_pojisteneho", model);
    }

    @GetMapping(path = "/novy")
    public ModelAndView novyPojistenec(Map<String, Object> model) {
        model.put("titulek", "Nový pojištěncec");
        return new ModelAndView("novy_pojistenec", model);
    }

    @PostMapping(path = "/novy", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public RedirectView ulozeniPojistence(PojistenecDto pojistenecDto) {
        repository.save(Pojistenec.builder()
                .jmeno(pojistenecDto.getJmeno())
                .prijmeni(pojistenecDto.getPrijmeni())
                .email(pojistenecDto.getEmail())
                .uliceACislo(pojistenecDto.getUliceACislo())
                .psc(pojistenecDto.getPsc())
                .telefon(pojistenecDto.getTelefon())
                .mesto(pojistenecDto.getMesto())
                .build());
        textNotifikace = "Pojištěnec byl uložen";
        return new RedirectView("/pojistenci/1/true");
    }

    @GetMapping(path = "/vygeneruj-pojistence")
    public void testovaciData() {
        for(int i = 1; i <= 20; i++) {
            Pojistenec pojistenec = TestDataGenerator.vygenerujPojistence(i);
            repository.save(pojistenec);
        }
    }

    @PostMapping(path = "/smazat/{id}")
    public RedirectView smazatPojistence(@PathVariable Long id) {
        repository.deleteById(id);
        textNotifikace = "Pojištěnec vymazán";
        return new RedirectView("/pojistenci/1/true");
    }

    @GetMapping(path = "/editovat/{id}")
    public ModelAndView editovatPojistence(@PathVariable Long id, Map<String, Object> model) {
        model.put("titulek", "Editace pojištěnce");
        model.put("pojistenec", repository.findById(id).get());
        return new ModelAndView("editace_pojisteneho", model);
    }

    @PostMapping(path = "/ulozit/{id}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public RedirectView ulozitPojistence(PojistenecDto pojistenecDto, @PathVariable Long id) {
        repository.save(Pojistenec.builder()
                .id(id)
                .email(pojistenecDto.getEmail())
                .jmeno(pojistenecDto.getJmeno())
                .prijmeni(pojistenecDto.getPrijmeni())
                .uliceACislo(pojistenecDto.getUliceACislo())
                .mesto(pojistenecDto.getMesto())
                .psc(pojistenecDto.getPsc())
                .telefon(pojistenecDto.getTelefon())
                .build()
        );
        return new RedirectView("/pojistenci/detail/" + id);
    }
}

package cz.rekvalifikace.projekt.controller;

import cz.rekvalifikace.projekt.model.Pojistenec;
import cz.rekvalifikace.projekt.model.Pojisteni;
import cz.rekvalifikace.projekt.repository.PojistenecRepository;
import cz.rekvalifikace.projekt.repository.PojisteniRepository;
import cz.rekvalifikace.projekt.view.Option;
import cz.rekvalifikace.projekt.view.PojisteniDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping(value = "/pojisteni")
public class PojisteniController {

    @Autowired
    private PojisteniRepository repository;

    @Autowired
    private PojistenecRepository pojistenecRepository;

    private Random random = new Random();
    private static final String FORMAT_DATA = "yyyy-MM-dd";

    @GetMapping(path = "/detail/{id}")
    public ModelAndView detailPojisteni(Map<String, Object> model, @PathVariable Long id) {
        Pojisteni pojisteni = repository.findById(id).get();
        model.put("titulek", "Detail pojištění");
        model.put("pojisteni", pojisteni);
        return new ModelAndView("detail_pojisteni", model);
    }

    @GetMapping(path = "/nove/{pojistenecId}")
    public ModelAndView novePojisteni(Map<String, Object> model, @PathVariable Long pojistenecId) {
        model.put("titulek", "Nové pojištění");
        model.put("pojistenecId", pojistenecId);
        model.put("typPojisteniOptions", Option.vytvorHodnotySelectBoxu(Pojisteni.Typ.values()));
        model.put("predmetPojisteniOptions", Option.vytvorHodnotySelectBoxu(Pojisteni.MajetkovePojisteni.values()));
        return new ModelAndView("nove_pojisteni", model);
    }

    @GetMapping(path = "/hodnoty-selectboxu/{hodnota}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> hodnotySelectBoxu(@PathVariable String hodnota) {
        Map<String, String> kliceAHodnoty = new HashMap<>();
        if (hodnota.equals(Pojisteni.Typ.POJISTENI_ZDRAVI.name())) {
            for(Pojisteni.ZdravotniPojisteni pojisteni : Pojisteni.ZdravotniPojisteni.values()) {
                kliceAHodnoty.put(pojisteni.name(), pojisteni.toString());
            }
        } else {
            for (Pojisteni.MajetkovePojisteni pojisteni : Pojisteni.MajetkovePojisteni.values()) {
                kliceAHodnoty.put(pojisteni.name(), pojisteni.toString());
            }
        }
        return new ResponseEntity<>(kliceAHodnoty, HttpStatus.OK);
    }

    @PostMapping(path = "/ulozit")
    public RedirectView ulozitPojisteni(PojisteniDto pojisteniDto) {
        repository.save(
                Pojisteni.builder()
                        .pojistenec(pojistenecRepository.findById(Long.valueOf(pojisteniDto.getPojistenecId())).get())
                        .id(pojisteniDto.getPojisteniId())
                        .typPojisteni(pojisteniDto.getTypPojisteni())
                        .platnostOd(stringToDate(pojisteniDto.getPlatnostOd()))
                        .platnostDo(stringToDate(pojisteniDto.getPlatnostDo()))
                        .predmet(pojisteniDto.getPredmetPojisteni())
                        .castkaVHalerich(Long.valueOf(pojisteniDto.getCastkaVHalerich()))
                        .build()
        );
        return new RedirectView("/pojistenci/detail/" + pojisteniDto.getPojistenecId());
    }

    private static LocalDate stringToDate(String datumACas) {
        String pouzeDatum = datumACas.substring(0, 10);
        return LocalDate.parse(pouzeDatum, DateTimeFormatter.ofPattern(FORMAT_DATA));
    }

    @GetMapping(path = "/editovat/{id}")
    public ModelAndView editovatPojisteni(@PathVariable Long id, Map<String, Object> model) {
        Pojisteni pojisteni = repository.findById(id).get();
        model.put("titulek", "Editace pojištění");
        model.put("pojisteni", pojisteni);
        model.put("typyPojisteni", Option.vytvorHodnotySelectBoxu(Pojisteni.Typ.values(), pojisteni.getTypPojisteniKlic()));
        if(pojisteni.getTypPojisteniKlic().equals("POJISTENI_MAJETKU")) {
            final List<Option> predmetOptions =
                    Option.vytvorHodnotySelectBoxu(Pojisteni.MajetkovePojisteni.values(), pojisteni.getPredmetPojisteniKlic());
            model.put("predmetPojisteni", predmetOptions);
        } else {
            final List<Option> predmetOptions =
                    Option.vytvorHodnotySelectBoxu(Pojisteni.ZdravotniPojisteni.values(), pojisteni.getPredmetPojisteniKlic());
            model.put("predmetPojisteni", predmetOptions);
        }

        return new ModelAndView("editace_pojisteni", model);
    }

    @PostMapping(path = "/smazat/{id}")
    public RedirectView smazPojisteni(@PathVariable Long id) {
        Long idPojistence = pojistenecRepository.findByPojisteniId(id).getId();
        repository.deleteById(id);
        return new RedirectView("/pojistenci/detail/" + idPojistence);
    }

    @GetMapping(path = "/vygeneruj-pojisteni")
    public void testovaciData() {
        Pageable limit = PageRequest.of(0, 20);
        Iterator<Pojistenec> pojistenci = pojistenecRepository.findAll(limit).iterator();
        for (int i = 1; i <= 20; i++) {
            String predmetPojisteni;
            String typ = Pojisteni.Typ.class.getEnumConstants()[random.nextInt(Pojisteni.Typ.values().length)].name();

            if (typ.equals("POJISTENI_MAJETKU")) {
                predmetPojisteni = Pojisteni.MajetkovePojisteni
                        .values()[random.nextInt(Pojisteni.MajetkovePojisteni.values().length)]
                        .name();
            } else {
                predmetPojisteni = Pojisteni.ZdravotniPojisteni
                        .values()[random.nextInt(Pojisteni.ZdravotniPojisteni.values().length)]
                        .name();
            }

            Pojisteni pojisteni = Pojisteni.builder()
                    .pojistenec(pojistenci.next())
                    .typPojisteni(typ)
                    .castkaVHalerich(10000L + (i * 12))
                    .predmet(predmetPojisteni)
                    .platnostOd(LocalDate.now())
                    .platnostDo(LocalDate.now().plusYears(1))
                    .build();
            repository.save(pojisteni);
        }
    }

}

package cz.rekvalifikace.projekt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Pojisteni {

    public enum Typ {
        POJISTENI_MAJETKU("Pojištění majetku"),
        POJISTENI_ZDRAVI("Zdravotní pojištění");

        private final String nazev;

        Typ(String nazev) {
            this.nazev = nazev;
        }

        @Override
        public String toString() {
            return nazev;
        }
    }

    public enum MajetkovePojisteni {
        POJISTENI_BYTU("Byt"),
        POJISTENI_AUTA("Povinné ručení");

        private final String nazev;

        MajetkovePojisteni(String nazev) {
            this.nazev = nazev;
        }

        @Override
        public String toString() {
            return nazev;
        }
    }

    public enum ZdravotniPojisteni {
        POJISTENI_URAZU("Úrazové pojištění"),
        POJISTENI_CESTOVNI("Cestovní pojištění");

        private final String nazev;

        ZdravotniPojisteni(String nazev) {
            this.nazev = nazev;
        }

        @Override
        public String toString() {
            return nazev;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pojistenec_id", nullable = false)
    private Pojistenec pojistenec;

    private String typPojisteni;
    private Long castkaVHalerich;
    private String predmet;
    private LocalDate platnostOd;
    private LocalDate platnostDo;

    @Override
    public String toString() {
        return "Pojisteni{" + "id=" + id + ", typPojisteni='" + typPojisteni + '\'' + ", castkaVHalerich=" + castkaVHalerich + ", predmet='" + predmet + '\'' + ", platnostOd=" + platnostOd + ", platnostDo="
                + platnostDo + '}';
    }

    public String getTypPojisteniKlic() {
        return typPojisteni;
    }

    public String getTypPojisteniHodnota() {
        return Typ.valueOf(typPojisteni).toString();
    }

    public String getPredmetPojisteniKlic() {
        return predmet;
    }

    public String getPredmetPojisteniHodnota() {
        final List<String> zdravotni =
                Arrays.stream(ZdravotniPojisteni.values()).map(Enum::name).collect(Collectors.toList());
        final List<String> majetkove =
                Arrays.stream(MajetkovePojisteni.values()).map(Enum::name).collect(Collectors.toList());

        if (zdravotni.contains(predmet)) {
            return ZdravotniPojisteni.valueOf(predmet).toString();
        }
        if (majetkove.contains(predmet)) {
            return MajetkovePojisteni.valueOf(predmet).toString();
        }
        return "-";
    }
}

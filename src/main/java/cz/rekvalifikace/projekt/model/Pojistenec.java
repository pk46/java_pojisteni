package cz.rekvalifikace.projekt.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Pojistenec {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional=false)
    @Column(updatable=false)
    private Long id;

    @OneToMany(mappedBy = "pojistenec", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Pojisteni> pojisteni;

    private String jmeno;
    private String prijmeni;
    private String uliceACislo;
    private String psc;
    private String mesto;
    private String email;
    private String telefon;
    private String hesloHash;

    @Override
    public String toString() {
        return "Pojistenec{" +
                "id=" + id +
                ", jmeno='" + jmeno + '\'' +
                ", prijmeni='" + prijmeni + '\'' +
                ", uliceACislo='" + uliceACislo + '\'' +
                ", psc='" + psc + '\'' +
                ", mesto='" + mesto + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }

}

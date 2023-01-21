package cz.rekvalifikace.projekt.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class PojistenecDto {
    private String id;
    private String jmeno;
    private String prijmeni;
    private String uliceACislo;
    private String psc;
    private String mesto;
    private String email;
    private String telefon;
}

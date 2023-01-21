package cz.rekvalifikace.projekt.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class PojisteniDto {
    private Long pojistenecId;
    private Long pojisteniId;
    private String typPojisteni;
    private String castkaVHalerich;
    private String predmetPojisteni;
    private String platnostOd;
    private String platnostDo;

}

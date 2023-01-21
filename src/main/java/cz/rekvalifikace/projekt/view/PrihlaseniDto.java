package cz.rekvalifikace.projekt.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrihlaseniDto {
    private String email;
    private String heslo;
}

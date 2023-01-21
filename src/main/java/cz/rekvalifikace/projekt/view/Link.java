package cz.rekvalifikace.projekt.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Link {

    private String url;
    private String text;
    private String css;
    private int tabIndex;

}

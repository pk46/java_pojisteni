package cz.rekvalifikace.projekt.service;

import cz.rekvalifikace.projekt.model.Pojistenec;

import java.util.Random;

public class TestDataGenerator
{
    private static final Random RANDOM = new Random();
    private static final String[] PREFIXES = {"602", "608", "721"};
    public static Pojistenec vygenerujPojistence(int id) {
        return Pojistenec.builder()
                .email("user"+id+"@projekt.cz")
                .psc("50331")
                .jmeno("Jméno"+id)
                .mesto("Pardubice")
                .prijmeni("Příjmení"+id)
                .telefon(vygenerujTelefon())
                .uliceACislo("Staroměstská 210"+id)
                .build();
    }

    private static String vygenerujTelefon() {
        StringBuilder telefon = new StringBuilder();
        telefon.append(PREFIXES[RANDOM.nextInt(PREFIXES.length)]);
        for(int i = 0; i < 6; i++) {
            telefon.append(RANDOM.nextInt(10));
        }
        return telefon.toString();
    }
}

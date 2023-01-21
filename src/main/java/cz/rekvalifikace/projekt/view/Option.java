package cz.rekvalifikace.projekt.view;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Option {

    public String key;
    public String name;
    public String selected;

    public Option(String key, String name, boolean selected) {
        this.key = key;
        this.name = name;
        this.selected = selected ? "selected" : "";
    }

    public static <T extends Enum<T>> List<Option> vytvorHodnotySelectBoxu(T[] values) {
        return vytvorHodnotySelectBoxu(values, "");
    }

    public static <T extends Enum<T>> List<Option> vytvorHodnotySelectBoxu(final T[] values, final String selectedValue) {
        return Stream
                .of(values)
                .map(item -> new Option(item.name(), item.toString(), item.name().equals(selectedValue)))
                .collect(Collectors.toList());
    }
}

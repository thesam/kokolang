package se.samuelmoritz.koko.support;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    private List<String> identifiers = new ArrayList<>();

    public void add(String id) {
        identifiers.add(id);
    }

    public boolean exists(String id) {
        return identifiers.contains(id);
    }
}

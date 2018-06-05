package com.company.support;

import java.util.ArrayList;
import java.util.List;

public class Context {

    public static Context current = new Context(null);

    private Context parent;
    List<Context> children = new ArrayList<>();
    List<String> identifiers = new ArrayList<>();

    private Context(Context parent) {
        this.parent = parent;
    }

    public void enterBlock() {
        current = new Context(this);
        children.add(current);
    }

    public void exitBlock() {
        current = this.parent;
    }

    public void add(String id) {
        System.out.println("Add id: " + id);
        identifiers.add(id);
    }

    public boolean exists(String id) {
        Context context = this;
        boolean found = identifiers.contains(id);
        while (!found && context != null) {
            found = identifiers.contains(id);
            context = this.parent;
        }
        return found;
    }
}

package com.company.support;

import java.util.ArrayList;
import java.util.List;

public class Context {

    public static Context current = new Context(null);

    private Context parent;
    List<Context> children = new ArrayList<>();
    List<String> identifiers = new ArrayList<>();
    int currentIndex = 0;

    public static void reset() {
        current = new Context(null);
    }

    private Context(Context parent) {
        this.parent = parent;
    }

    public void enterNewBlock() {
        current = new Context(this);
        children.add(current);
    }

    public void enterNextBlock() {
        current = children.get(currentIndex++);
        //TODO: Must enter next existing block in the correct order
        // Maybe save iteration index in each context?
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
        boolean found = context.identifiers.contains(id);
        while (!found && context != null) {
            found = context.identifiers.contains(id);
            context = context.parent;
        }
        return found;
    }
}

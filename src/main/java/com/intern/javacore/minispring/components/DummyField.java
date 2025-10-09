package com.intern.javacore.minispring.components;

import com.intern.javacore.minispring.framework.Component;

@Component
public class DummyField {
    public DummyField() {}

    public String dumbify(String thing) {
        return "Dumb " + thing;
    }
}

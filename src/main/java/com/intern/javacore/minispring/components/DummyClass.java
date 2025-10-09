package com.intern.javacore.minispring.components;

import com.intern.javacore.minispring.framework.Autowired;
import com.intern.javacore.minispring.framework.Component;

@Component
public class DummyClass {

    @Autowired
    private DummyField myField;

    public DummyClass() {}

    public String getDumb(String thing) {
        return myField.dumbify(thing);
    }
}

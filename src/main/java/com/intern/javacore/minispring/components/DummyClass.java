package com.intern.javacore.minispring.components;

import com.intern.javacore.minispring.framework.Autowired;
import com.intern.javacore.minispring.framework.Component;
import com.intern.javacore.minispring.framework.InitializingBean;

@Component
public class DummyClass implements InitializingBean {

    @Autowired
    private DummyField myField;
    private boolean ready;

    @Override
    public void afterPropertiesSet() {
        ready = true;
        System.out.println("Dummy is ready !!");
    }

    public DummyClass() {}

    public String getDumb(String thing) {
        if (!ready) throw new IllegalStateException("not ready");
        return myField.dumbify(thing);
    }


}

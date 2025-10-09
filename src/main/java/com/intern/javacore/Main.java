package com.intern.javacore;

import com.intern.javacore.minispring.components.DummyPrototypeClass;
import com.intern.javacore.minispring.framework.MiniApplicationContext;
import com.intern.javacore.minispring.components.DummyClass;


public class Main {
    public static void main(String[] args) {
        MiniApplicationContext ctx =
                new MiniApplicationContext("com.intern.javacore.minispring.components");
        DummyClass myClass = ctx.getBean(DummyClass.class);

        // afterPropertiesSet() is called for each
        DummyPrototypeClass prototype1 = ctx.getBean(DummyPrototypeClass.class);
        DummyPrototypeClass prototype2 = ctx.getBean(DummyPrototypeClass.class);

        System.out.println(prototype1.hashCode() == prototype2.hashCode()); // false
        System.out.println(prototype1.equals(prototype2)); // false


        System.out.println(myClass.getDumb("Dependency Injection"));
    }
}

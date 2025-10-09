package com.intern.javacore;

import com.intern.javacore.minispring.framework.MiniApplicationContext;
import com.intern.javacore.minispring.components.DummyClass;


public class Main {
    public static void main(String[] args) {
        MiniApplicationContext ctx =
                new MiniApplicationContext("com.intern.javacore.minispring.components");
        DummyClass myClass = ctx.getBean(DummyClass.class);

        System.out.println(myClass.getDumb("Dependency Injection"));;
    }
}

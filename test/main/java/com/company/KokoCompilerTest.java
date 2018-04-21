package com.company;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class KokoCompilerTest {

    @Test
    public void shouldCompileEmptyString() throws Exception {
        Class clazz = new KokoCompiler().compile("");
        assertNotNull(clazz);
    }

    @Test
    public void canAssignStringToVariable() throws Exception {
        Class clazz = new KokoCompiler().compile("*x = \"5\"");
        Object instance = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("main")) {
                method.invoke(instance);
            }
        }
    }

}
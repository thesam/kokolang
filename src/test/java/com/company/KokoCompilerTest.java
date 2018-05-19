package com.company;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KokoCompilerTest {

    @Test
    public void shouldGenerateClassfile() throws Exception {
        Class clazz = compile("");
        assertNotNull(clazz);
    }

    @Test
    public void shouldExportFunction() {
        Class clazz = compile("myfunc void");
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        assertEquals(1,methods.size());
        assertEquals("myfunc",methods.get(0).getName());
    }

    private Class compile(String input) {
        return new KokoCompiler().compile(input);
    }

    @Test
    public void shouldGenerateFunctionBody() {
        compile("myfunc int" + "\n" + "\tret 5");
    }

}
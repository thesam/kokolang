package com.company;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
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
    public void shouldExportFunctionWithBody() throws InvocationTargetException, IllegalAccessException {
        Class clazz = compile("myfunc int\n\tret 0");
        //TODO: Create instance of clazz?
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        assertEquals(1,methods.size());
        assertEquals("myfunc",methods.get(0).getName());
        Object result = methods.get(0).invoke(null);
        assertEquals(0,result);
    }

    private Class compile(String input) {
        return new KokoCompiler().compile(input);
    }
}
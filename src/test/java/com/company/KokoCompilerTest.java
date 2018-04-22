package com.company;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KokoCompilerTest {

    @Test
    public void shouldGenerateClassfile() throws Exception {
        Class clazz = new KokoCompiler().compile("");
        assertNotNull(clazz);
    }

    @Test
    public void shouldExportFunction() {
        Class clazz = new KokoCompiler().compile("myfunc void");
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        assertEquals(1,methods.size());
        assertEquals("myfunc",methods.get(0).getName());
    }

}
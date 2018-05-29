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
        Class clazz = compileSuccess("");
        assertNotNull(clazz);
    }

    @Test
    public void shouldExportFunctionWithBody() throws InvocationTargetException, IllegalAccessException {
        Class clazz = compileSuccess("myfunc int\n\tret 0");
        //TODO: Create instance of clazz?
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        assertEquals(1,methods.size());
        assertEquals("myfunc",methods.get(0).getName());
        Object result = methods.get(0).invoke(null);
        assertEquals(0,result);
    }

    @Test
    public void canCallFunction() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = compileSuccess("myfunc int\n\tret 0\n\nmyfunc2 int\n\tret myfunc()");
        Method method = clazz.getDeclaredMethod("myfunc2");
        Object result = method.invoke(null);
        assertEquals(0,result);
    }

    @Test
    public void canNotCallFunctionThatDoesNotExist() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> errors = compileError("myfunc2 int\n\tret myfuncmissing()");
        assertEquals(1,errors.size());
        //Method method = clazz.getDeclaredMethod("myfunc2");
        //Object result = method.invoke(null);
        //assertEquals(0,result);
    }

    private Class compileSuccess(String input) {
        return new KokoCompiler().compile(input).compiledClass().get();
    }

    private List<String> compileError(String input) { return new KokoCompiler().compile(input).errors(); }
}
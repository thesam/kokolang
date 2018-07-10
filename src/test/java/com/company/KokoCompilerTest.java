package com.company;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class KokoCompilerTest {

    @Test
    public void shouldGenerateClassfile() throws Exception {
        Class clazz = compileSuccess("");
        assertNotNull(clazz);
    }

    @Test
    public void shouldExportFunctionWithBody() throws Exception {
        Class clazz = compileSuccess("myfunc int\n\tret 0");
        //TODO: Create instance of clazz?
        Method method = getMethod(clazz, "myfunc");
        Object result = method.invoke(null);
        assertEquals(0,result);
    }

    @Test
    public void canCallFunction() throws Exception {
        Class clazz = fixtureSuccess("call_function");
        Method method = getMethod(clazz, "myfunc2");
        Object result = method.invoke(null);
        assertEquals(0,result);
    }

    private Method getMethod(Class clazz, String functionName) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(functionName);
    }


    @Test
    public void canNotCallFunctionThatDoesNotExist() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> errors = compileError("myfunc2 int\n\tret myfuncmissing()");
        assertError(errors,"myfuncmissing");
    }

    @Test
    public void canAssignVariable() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = compileSuccess("myfunc int\n\tint x = 5\n\tret x");
        Method method = getMethod(clazz, "myfunc");
        Object result = method.invoke(null);
        assertEquals(5,result);
    }

    @Test
    public void errorUndefinedVariable() throws Exception {
        List<String> errors = fixtureError("undefined_variable");
        assertError(errors,"x");
    }

    @Test
    public void errorUndefinedVariableWrongScope() throws Exception {
        List<String> errors = fixtureError("undefined_variable_wrong_scope");
        assertError(errors,"x");
    }


    private Class compileSuccess(String input) {
        CompilerResult result = new KokoCompiler().compile(input);
        return result.compiledClass().orElseThrow(() -> new RuntimeException("Compiler errors: " + result.errors()));
    }

    private List<String> compileError(String input) { return new KokoCompiler().compile(input).errors(); }

    private Class fixtureSuccess(String fixtureName) throws IOException {
        Path path = Paths.get("src","test","resources",fixtureName + ".koko");
        byte[] bytes = Files.readAllBytes(path);
        String input = new String(bytes);
        return compileSuccess(input);
    }

    private List<String> fixtureError(String fixtureName) throws IOException {
        Path path = Paths.get("src","test","resources",fixtureName + ".koko");
        byte[] bytes = Files.readAllBytes(path);
        String input = new String(bytes);
        return compileError(input);
    }

    private void assertError(List<String> errors, String partialMessage) {
        assertEquals(1,errors.size());
        assertTrue(errors.get(0).contains(partialMessage));
    }
}

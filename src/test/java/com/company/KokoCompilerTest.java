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
        runFixture("call_function");
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
        runFixture("undefined_variable");
    }

    @Test
    public void errorUndefinedVariableWrongScope() throws Exception {
        runFixture("undefined_variable_wrong_scope");
    }


    private Class compileSuccess(String input) {
        CompilerResult result = new KokoCompiler().compile(input);
        return result.compiledClass().orElseThrow(() -> new RuntimeException("Compiler errors: " + result.errors()));
    }

    private List<String> compileError(String input) { return new KokoCompiler().compile(input).errors(); }

    private void runFixture(String fixtureName) throws Exception {
        Path path = Paths.get("src","test","resources",fixtureName + ".koko");
        byte[] bytes = Files.readAllBytes(path);
        String input = new String(bytes);
        String[] lines;
        boolean successExpected = true;
        if (input.contains("RET:")) {
            lines = input.split("RET:");
        } else if (input.contains("ERR:")) {
            lines = input.split("ERR:");
            successExpected = false;
        } else {
            throw new RuntimeException("Malformed test");
        }
        CompilerResult result = new KokoCompiler().compile(lines[0]);
        if (successExpected) {
            Method main = getMethod(result.compiledClass().get(), "main");
            assertEquals(lines[1], main.invoke(null).toString());
        } else {
            assertEquals(lines[1], result.errors().get(0));
        }
    }

    private void assertError(List<String> errors, String partialMessage) {
        assertEquals(1,errors.size());
        assertTrue(errors.get(0).contains(partialMessage));
    }
}

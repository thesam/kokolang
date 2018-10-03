package com.company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class KokoCompilerTest {

    private Path fixture;

    @Parameterized.Parameters(name="{0}")
    public static Collection fixtures() throws Exception {
        List<Path> fixturePaths = Files.find(Paths.get("src", "test", "resources"), Integer.MAX_VALUE, (path, fileAttrs) -> {
            return path.toString().endsWith(".kokot");
        }).collect(Collectors.toList());
        return fixturePaths;
    }

    public KokoCompilerTest(Path fixture) {
        this.fixture = fixture;
    }

    @Test
    public void runFixture() throws Exception {
        runFixture(this.fixture);
    }

    private Method getMethod(Class clazz, String functionName) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(functionName);
    }

    private void runFixture(Path fixture) throws Exception {
        byte[] bytes = Files.readAllBytes(fixture);
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
            Method main = getMethod(result.compiledClass().orElseThrow(() -> new RuntimeException("Unexpected compiler error: " + result.errors().get(0))), "main");
            assertEquals(lines[1], main.invoke(null).toString());
        } else {
            assertEquals(lines[1], result.errors().get(0));
        }
    }
}

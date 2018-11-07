package se.samuelmoritz.koko;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class KokoCompilerTest {

    private Path fixture;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Path> fixtures() throws Exception {
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

    private Method getMainMethod(Class clazz) throws NoSuchMethodException {
        return clazz.getDeclaredMethod("main", String[].class);
    }

    private void runFixture(Path fixture) throws Exception {
        byte[] bytes = Files.readAllBytes(fixture);
        String input = new String(bytes);
        String[] lines = input.split("#");

        CompilerResult result = new KokoCompiler().compile(lines[0]);
        if (lines[1].startsWith("STDOUT:")) {
            Method main = getMainMethod(result.compiledClass().orElseThrow(() -> new RuntimeException("Unexpected compiler error: " + result.errors().get(0))));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            String[] args = null;
            main.invoke(null, (Object) args);
            assertEquals(lines[1].replaceFirst("[A-Z]+:(\n|\r\n)", ""), baos.toString());
        } else if (lines[1].startsWith("ERROR:")) {
            String firstError = result.errors().get(0);
            String expectedError = lines[1].replaceFirst("ERROR:", "").replaceFirst("(\n|\r\n)[\\d\\D]*","");
            assertEquals(expectedError, firstError);
        } else {
            fail("Failed to parse test file content: " + lines[1]);
        }
    }
}

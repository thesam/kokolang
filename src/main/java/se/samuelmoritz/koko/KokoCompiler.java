package se.samuelmoritz.koko;

import se.samuelmoritz.koko.support.Context;
import se.samuelmoritz.koko.support.ErrorHandler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KokoCompiler {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args[0]));
        new KokoCompiler().compile(String.join("\n", lines));
    }

    public CompilerResult compile(String input) {
        input = input.split("#STDOUT:")[0];
        ErrorHandler.errors = new ArrayList<>();
        Context.reset();

        KokoLexer lexer = new KokoLexer(new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KokoParser parser = new KokoParser(tokens);

        ParseTree tree = parser.prog();
        runSemanticChecking(tree);

        if (ErrorHandler.errors.size() > 0) {
            return new CompilerResult(null, ErrorHandler.errors);
        }

        Class cls = generateCode(tree);

        return new CompilerResult(cls, new ArrayList<>());
    }

    private Class generateCode(ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();
        boolean hasFunctionDeclaration = !((KokoParser.ProgContext) tree).functionDeclaration().isEmpty();

        JavaGenerator javaGenerator = new JavaGenerator(hasFunctionDeclaration);
        walker.walk(javaGenerator, tree);

        compileToBytecode(javaGenerator.output, javaGenerator.className);
        return loadClass(javaGenerator.className);
    }

    private void runSemanticChecking(ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ContextListener(), tree);
        walker.walk(new SemanticChecker(), tree);
    }

    private Class loadClass(String className) {
        try {
            File file = new File(".");
            URL url = null;          // file:/c:/myclasses/
            url = file.toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls);
            return cl.loadClass(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void compileToBytecode(String javaSource, String className) {
        System.out.println(javaSource);
        String javaFilename = className + ".java";
        saveToJavaFile(javaSource, javaFilename);
        compileToClassFile(javaFilename);
        try {
            Files.delete(Paths.get(javaFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void compileToClassFile(String filename) {
        File[] files1 = {new File(filename)};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        Boolean compilerResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
        if (!compilerResult) {
            throw new RuntimeException("Java compilation failed!");
        }
    }

    private void saveToJavaFile(String javaSource, String filename) {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(javaSource);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

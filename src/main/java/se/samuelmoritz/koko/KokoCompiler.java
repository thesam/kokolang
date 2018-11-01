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
    public CompilerResult compile(String input) {
        try {
            input = input.split("#STDOUT:")[0];
            ErrorHandler.errors = new ArrayList<>();
            Context.reset();
            KokoLexer lexer = new KokoLexer(new ANTLRInputStream(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            KokoParser parser = new KokoParser(tokens);
            ParseTree tree = parser.prog();
            ParseTreeWalker walker = new ParseTreeWalker();
            JavaGenerator javaGenerator = new JavaGenerator();
            List<KokoListener> listeners = Arrays.asList(
                    new ContextListener(),
                    new SemanticChecker(),
                    javaGenerator
            );
            for (KokoListener listener : listeners) {
                walker.walk(listener, tree);
                if (ErrorHandler.errors.size() > 0) {
                    return new CompilerResult(null, ErrorHandler.errors);
                }
            }

            compileToBytecode(javaGenerator.output);

            // Create a File object on the root of the directory containing the class file
            File file = new File(".");

            // Convert File to a URL
            URL url = file.toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            // Load in the class; MyClass.class should be located in
            // the directory file:/c:/myclasses/com/mycompany
            Class cls = cl.loadClass("Main");
            Files.delete(Paths.get("Main.java"));
            Files.delete(Paths.get("Main.class"));
            return new CompilerResult(cls, new ArrayList<>());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void compileToBytecode(String javaSource) {
        System.out.println(javaSource);
        try {
            FileWriter fileWriter = new FileWriter("Main.java");
            fileWriter.write(javaSource);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File[] files1 = {new File("Main.java")};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        Boolean compilerResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
        System.out.println(compilerResult);
    }
}

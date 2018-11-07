package se.samuelmoritz.koko;

import se.samuelmoritz.koko.support.SymbolTable;
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
import java.util.Arrays;
import java.util.List;

public class KokoCompiler {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args[0]));
        new KokoCompiler().compile(String.join("\n", lines));
    }

    CompilerResult compile(String sourceCode) {
        String lexerInput = removeComments(sourceCode);

        KokoLexer lexer = new KokoLexer(new ANTLRInputStream(lexerInput));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        KokoParser parser = new KokoParser(tokens);
        ParseTree parseTree = parser.prog();

        List<String> semanticErrors = runSemanticChecking(parseTree);

        if (semanticErrors.isEmpty()) {
            Class compiledClass = generateCode(parseTree);
            return CompilerResult.success(compiledClass);
        } else {
            return CompilerResult.failed(semanticErrors);
        }
    }

    private Class generateCode(ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();
        boolean hasFunctionDeclaration = !((KokoParser.ProgContext) tree).functionDeclaration().isEmpty();

        JavaGenerator javaGenerator = new JavaGenerator(hasFunctionDeclaration);
        walker.walk(javaGenerator, tree);

        compileToBytecode(javaGenerator.output, javaGenerator.className);
        return loadClass(javaGenerator.className);
    }

    private List<String> runSemanticChecking(ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();
        SymbolTableCreator symbolTableCreator = new SymbolTableCreator();
        walker.walk(symbolTableCreator, tree);
        SemanticChecker semanticChecker = new SemanticChecker(symbolTableCreator.symbolTable);
        walker.walk(semanticChecker, tree);
        return semanticChecker.errors;
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
        //System.out.println(javaSource);
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

    private String removeComments(String input) {
        input = input.split("#")[0];
        return input;
    }
}

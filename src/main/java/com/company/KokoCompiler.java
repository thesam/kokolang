package com.company;

import com.generated.KokoLexer;
import com.generated.KokoParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KokoCompiler {
    public Class compile(String input) {
        try {
        KokoLexer lexer = new KokoLexer(new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        KokoParser parser = new KokoParser( tokens );
        ParseTree tree = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new JavaEmittingKokoListener(), tree );

        // Create a File object on the root of the directory containing the class file
        File file = new File(".");

            // Convert File to a URL
            URL url = file.toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            // Load in the class; MyClass.class should be located in
            // the directory file:/c:/myclasses/com/mycompany
            Class cls = cl.loadClass("Foo");
            Files.delete(Paths.get("Foo.java"));
            Files.delete(Paths.get("Foo.class"));
            return cls;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

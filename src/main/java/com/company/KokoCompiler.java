package com.company;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import com.generated.*;

public class KokoCompiler {
    public Class compile(String input) {
        try {
        ExprLexer lexer = new ExprLexer(new ANTLRFileStream("example.foo"));
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        ExprParser parser = new ExprParser( tokens );
        ParseTree tree = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new JavaEmittingExprListener(), tree );

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

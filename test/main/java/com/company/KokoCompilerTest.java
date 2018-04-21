package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class KokoCompilerTest {

    @Test
    public void shouldCompileEmptyString() {
        Class clazz = new KokoCompiler().compile("");
        assertNotNull(clazz);
    }

}
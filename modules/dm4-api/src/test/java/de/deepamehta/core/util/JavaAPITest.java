package de.deepamehta.core.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class JavaAPITest {

    @Test
    public void asListWithNullArgument() {
        try {
            Arrays.asList((Object[]) null);
            fail();
        } catch (NullPointerException e) {
            // => asList can't take a null argument
        }
    }

    @Test
    public void asListWithNoArguments() {
        assertSame(0, Arrays.asList().size());
    }

    @Test
    public void regex() throws Exception {
        String index = "/client/(index\\.html)?";
        assertTrue("/client/".matches(index));
        assertTrue("/client/index.html".matches(index));
        assertFalse("/client/login".matches(index));

        String notSystem = "/(?!system).*";
        assertTrue("/core".matches(notSystem));
        assertFalse("/system/console/bundles".matches(notSystem));
    }

}

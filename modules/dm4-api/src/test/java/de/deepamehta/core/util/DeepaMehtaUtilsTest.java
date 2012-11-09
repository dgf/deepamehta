package de.deepamehta.core.util;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import de.deepamehta.core.Identifiable;
import de.deepamehta.core.model.TopicModel;

public class DeepaMehtaUtilsTest {

    @Test
    public void castClass() throws Exception {
        Object expected = new TopicModel(17);
        Identifiable actual = DeepaMehtaUtils.cast(expected);
        assertEquals(17, actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void castCollection() throws Exception {
        Object expected = new ArrayList<String>(Arrays.asList("check"));
        Collection<String> actual = DeepaMehtaUtils.cast(expected);
        for (String string : actual) {
            assertEquals("check", string);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void isDeepaMehtaURLinWebAppContext() throws Exception {
        // System.clearProperty("org.osgi.service.http.port");
        DeepaMehtaUtils.osgiPort = null;
        // System.setProperty("dm4.app.url", "/servlet/context");
        DeepaMehtaUtils.dmUrl = "/servlet/context";

        assertTrue(DeepaMehtaUtils.isDeepaMehtaURL(new URL(
                "http://localhost:8080/servlet/context/plugin/method")));
        assertFalse(DeepaMehtaUtils.isDeepaMehtaURL(new URL(
                "http://localhost:8080/servlet/other/context")));
    }

    @Test
    public void isDeepaMehtaURLinLocalContext() throws Exception {
        // System.clearProperty("dm4.app.url");
        DeepaMehtaUtils.dmUrl = null;
        // System.setProperty("org.osgi.service.http.port", "8080");
        DeepaMehtaUtils.osgiPort = "8080";

        String hostName = InetAddress.getLocalHost().getHostName();

        assertTrue(DeepaMehtaUtils.isDeepaMehtaURL(new URL(//
                "http://" + hostName + ":8080/plugin/method")));
        assertFalse("port", DeepaMehtaUtils.isDeepaMehtaURL(new URL(//
                "http://" + hostName + ":23/plugin/method")));
        assertFalse("hostname", DeepaMehtaUtils.isDeepaMehtaURL(new URL(//
                "http://UNKNOWN" + hostName + ":8080/plugin/method")));
    }
}

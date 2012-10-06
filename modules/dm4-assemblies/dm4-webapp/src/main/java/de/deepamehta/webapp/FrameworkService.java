package de.deepamehta.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.servlet.ServletContext;

import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 * Configure and start a OSGi framework.
 */
public final class FrameworkService {

    private static final String CONFIG_PROPERTIES = "/WEB-INF/config.properties";

    private static final String SYSTEM_PROPERTIES = "/WEB-INF/system.properties";

    private final ServletContext context;

    private Framework framework = null;

    public FrameworkService(ServletContext context) {
        this.context = context;
        log("initializing OSGi framework");

        Iterator<FrameworkFactory> load = ServiceLoader.load(FrameworkFactory.class).iterator();
        if (load.hasNext() == false) {
            throw new IllegalStateException("OSGi framework factory is not available");
        }

        try {
            // only one factory would be used
            FrameworkFactory factory = load.next();

            // merge system properties
            for (Entry<Object, Object> property : getProperties(SYSTEM_PROPERTIES).entrySet()) {
                String key = property.getKey().toString();
                String value = property.getValue().toString();
                log("add system property " + key + " = " + value);
                System.setProperty(key, value);
            }

            // load framework configuration properties
            Properties configuration = getProperties(CONFIG_PROPERTIES);
            // TODO extract a abstract service implementation
            // TODO separate a felix specific service distribution
            configuration.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP,
                    Arrays.asList(new ProvisionActivator(context)));
            for (Entry<Object, Object> property : configuration.entrySet()) {
                log("configuration " + property.getKey() + " = " + property.getValue());
            }

            // create framework instance
            framework = factory.newFramework(configuration);
        } catch (Exception e) {
            log("OSGi framework initialization failed", e);
        }
    }

    public void start() {
        log("starting OSGi framework");
        try {
            framework.start();
            log("OSGi framework started");
        } catch (Exception e) {
            log("OSGi framework failed to start", e);
        }
    }

    public void stop() {
        try {
            if (framework != null) {
                log("stopping OSGi framework");
                framework.stop();
                framework = null;
                log("OSGi framework stopped");
            }
        } catch (Exception e) {
            log("OSGi framework failed to stop", e);
        }
    }

    private Properties getProperties(String path) throws IOException {
        log("load property file " + path);
        InputStream stream = context.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalStateException("property file " + path + " not found");
        }
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

    private void log(String message) {
        context.log(message);
    }

    private void log(String message, Throwable cause) {
        context.log(message, cause);
    }
}

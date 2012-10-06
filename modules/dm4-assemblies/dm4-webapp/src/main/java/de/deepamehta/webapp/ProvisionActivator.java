package de.deepamehta.webapp;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class ProvisionActivator implements BundleActivator {

    private static final String BUNDLES = "/bundles";

    private final ServletContext servletContext;

    private final Set<URL> bundleUrls;

    public ProvisionActivator(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;

        Set<String> paths = cast(servletContext.getResourcePaths(BUNDLES));
        log("bundle paths " + paths);
        if (paths == null || paths.isEmpty()) {
            throw new IllegalStateException("empty bundle repository or wrong path configuration");
        }

        bundleUrls = new HashSet<URL>();
        for (String path : paths) {
            if (path.endsWith(".jar")) {
                URL url = servletContext.getResource(path);
                if (url == null) {
                    throw new IllegalStateException("bundle " + path + " resource not accessible");
                } else {
                    bundleUrls.add(url);
                }
            }
        }
    }

    @Override
    public void start(BundleContext context) throws Exception {
        log("activate provision");

        // save the bundle context
        servletContext.setAttribute(BundleContext.class.getName(), context);

        // install and start all bundles
        Set<Bundle> installed = new HashSet<Bundle>();
        for (URL url : bundleUrls) {
            installed.add(context.installBundle(url.toExternalForm()));
        }
        for (Bundle bundle : installed) {
            log("starting bundle " + bundle);
            bundle.start();
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        log("provision stopped");
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object o) {
        return (T) o;
    }

    private void log(String message) {
        servletContext.log(message);
    }
}

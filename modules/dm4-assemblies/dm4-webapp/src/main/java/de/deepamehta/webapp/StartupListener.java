package de.deepamehta.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class StartupListener implements ServletContextListener {

    private FrameworkService service;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        service = new FrameworkService(event.getServletContext());
        service.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        service.stop();
    }
}

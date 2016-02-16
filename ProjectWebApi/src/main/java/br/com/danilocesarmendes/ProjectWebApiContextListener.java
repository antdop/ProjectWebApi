package br.com.danilocesarmendes;

import br.com.danilocesarmendes.util.SessionFactoryUtil;
import java.util.Locale;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ProjectWebApiContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        Locale.setDefault(new Locale("pt", "BR"));

        SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
        SessionFactoryUtil.setInstance(sce.getServletContext(), sessionFactoryUtil);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactoryUtil.destroy(sce.getServletContext());
    }
}

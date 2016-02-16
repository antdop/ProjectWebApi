package br.com.danilocesarmendes.util;

import br.com.danilocesarmendes.model.Pessoa;
import br.com.danilocesarmendes.model.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.mindrot.jbcrypt.BCrypt;

public class SessionFactoryUtil implements SessionFactoryInterface {

    private Configuration cfg;
    private org.hibernate.SessionFactory sessionFactory;
    private Map<Session, Connection> connectionMap;
    private Connection connectionFactory;

    @Override
    public String getDatabaseType() {
        return "postgres";
    }

    public SessionFactoryUtil() {
        initiate();
    }

    public boolean isInitiated() {
        return cfg != null;
    }

    private void initiate() {
        if (cfg == null) {
            try {
                connectionMap = Collections.synchronizedMap(new LinkedHashMap<Session, Connection>());

                cfg = new Configuration();
                cfg.setProperty("hibernate.transaction.factory_class",
                        "org.hibernate.transaction.JDBCTransactionFactory");
                cfg.setProperty("hibernate.current_session_context_class", "thread");
                cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/project_api");
                cfg.setProperty("hibernate.connection.username", "postgres");
                cfg.setProperty("hibernate.connection.password", "a");
                cfg.setProperty("hibernate.hbm2dll.auto", "validate");
//                cfg.setProperty("hibernate.show_sql", "true");

                connectionFactory = ConnectionPostgreSQL("jdbc:postgresql://localhost:5432/project_api",
                        "postgres", "a", "org.postgresql.Driver");

                Class[] hibernateClass = new Class[]{
                    Usuario.class,
                    Pessoa.class
                };

                //<editor-fold defaultstate="collapsed" desc="prepara as entidades para ocupar apenas um banco de dados">
                for (Class clazz : hibernateClass) {
                    cfg.addAnnotatedClass(clazz);
                }
                //</editor-fold>

                sessionFactory = cfg.buildSessionFactory(new ServiceRegistryBuilder()
                        .applySettings(cfg.getProperties()).buildServiceRegistry());
                connectionFactory.close();
                updateDatabase();
            } catch (Exception ex) {
                cfg = null;
                throw new RuntimeException(ex);
            }
        }
    }

    private Connection ConnectionPostgreSQL(String url, String usuario, String senha, String driver) {

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateDatabase() {
        Connection con = openConnection();
        try {
            try {
                String sql = "CREATE SCHEMA api";
                con.createStatement().executeUpdate(sql);
                con.commit();
            } finally {
                con.close();
            }
        } catch (Exception ex) {
        }

        new SchemaUpdate(cfg).execute(true, true);

        Session s = openSession(openConnection());
        try {
            Criteria c = s.createCriteria(Usuario.class);
            if (c.list().isEmpty()) {
                s.beginTransaction();

                Usuario ucs = new Usuario();
                ucs.setEmail("admin");
                ucs.setNome("Administrador");
                ucs.setSenha(BCrypt.hashpw("a", BCrypt.gensalt()));
                s.save(ucs);

                s.getTransaction().commit();
            }
        } finally {
            releaseConnection(s);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator getAnnotatedClass() {
        return cfg.getClassMappings();
    }

    /**
     *
     * @return
     */
    public SessionFactory getInstance() {
        return sessionFactory;
    }

    @Override
    public Connection openConnection() {
        Connection c = connectionFactory = ConnectionPostgreSQL("jdbc:postgresql://localhost:5432/project_api",
                "postgres", "a", "org.postgresql.Driver");
        try {
            c.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return c;
    }

    /**
     * Opens a session and will not bind it to a session context
     *
     * @return the session
     */
    @Override
    public Session openSession(Connection connection) {
        SessionBuilder sb = sessionFactory.withOptions();
        Session session = sb.connection(connection).openSession();
        connectionMap.put(session, connection);
        return session;
    }

    @Override
    public void releaseConnection(Session session) {
        Connection connection = connectionMap.get(session);
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connectionMap.remove(session);
            }
            if (session.isOpen()) {
                session.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(SessionFactoryUtil.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * closes the session factory
     */
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }

        if (connectionFactory != null) {
            try {
                connectionFactory.close();
                connectionFactory = null;
            } catch (SQLException ex) {
                Logger.getLogger(SessionFactoryUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        cfg = null;
        connectionMap = null;
    }

    public static SessionFactoryUtil getInstance(ServletContext servletContext) {
        return (SessionFactoryUtil) servletContext.getAttribute("SessionFactoryUtil");
    }

    public static void setInstance(ServletContext servletContext, SessionFactoryUtil sessionFactoryUtil) {
        servletContext.setAttribute("SessionFactoryUtil", sessionFactoryUtil);
    }

    public static Connection openConnection(ServletContext sc) {
        return getInstance(sc).openConnection();
    }

    public static void releaseConnection(ServletContext sc, Session session) {
        getInstance(sc).releaseConnection(session);
    }

    public static Session openSession(ServletContext sc) {
        SessionFactoryUtil sfu = getInstance(sc);
        return sfu.openSession(sfu.openConnection());
    }

    public static String getDatabaseType(ServletContext sc) {
        return "postgres";
    }

    public static void destroy(ServletContext servletContext) {
        SessionFactoryUtil sfu = getInstance(servletContext);
        if (sfu != null) {
            sfu.destroy();
        }
    }
}

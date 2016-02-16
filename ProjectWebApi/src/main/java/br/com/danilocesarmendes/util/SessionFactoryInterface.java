
package br.com.danilocesarmendes.util;

import java.sql.Connection;
import java.util.Iterator;
import org.hibernate.Session;

public interface SessionFactoryInterface {

    public Iterator getAnnotatedClass();

    public String getDatabaseType();

    public Connection openConnection();

    public Session openSession(Connection cnctn);

    public void releaseConnection(Session sn);
}

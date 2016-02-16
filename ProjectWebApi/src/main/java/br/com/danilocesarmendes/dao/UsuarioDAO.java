package br.com.danilocesarmendes.dao;

import br.com.danilocesarmendes.model.Usuario;
import br.com.danilocesarmendes.util.SessionFactoryUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsuarioDAO {

    //  private Class<T> classe;
    private Session session;

    private Criteria criteria;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Boolean save(Usuario bean) {
        Boolean retorno = false;
        Transaction tx = null;
        try {
            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            tx = (Transaction) session.beginTransaction();
            session.save(bean);
            tx.commit();
            session.close();
            retorno = true;

        } catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> lista = new ArrayList<Usuario>();
        try {
            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            lista = (ArrayList<Usuario>) session.createQuery("from " + Usuario.class.getSimpleName()).list();

        } catch (Exception ex) {
        }
        return lista;
    }

    public Usuario getValidaUsuario(String user_login, String user_senha) {
        Usuario dep = new Usuario();
        try {

            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            
            Criteria cr = session.createCriteria(Usuario.class);
            cr.add(Restrictions.eq("email", user_login));
            cr.add(Restrictions.eq("senha", user_senha));
            dep = (Usuario) cr.uniqueResult();

            //    dep = (Usuario) session.get(Usuario.class, dep_id);         
        } catch (Exception ex) {
        }
        return dep;
    }

    public Usuario getUsuario(Integer id) {
        Usuario dep = new Usuario();
        try {

            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            dep = (Usuario) session.get(Usuario.class, id);

        } catch (Exception ex) {
        }
        return dep;
    }

}

package br.com.danilocesarmendes.dao;

import br.com.danilocesarmendes.model.Pessoa;
import br.com.danilocesarmendes.util.SessionFactoryUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PessoaDAO {

    private Session session;

    public Pessoa save(Pessoa pessoa) {
        Transaction tx = null;
        try {
            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            tx = (Transaction) session.beginTransaction();
            session.saveOrUpdate(pessoa);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pessoa;
    }

    public Pessoa remove(Pessoa pessoa) {
        Transaction tx = null;
        try {
            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            tx = (Transaction) session.beginTransaction();
            session.delete(pessoa);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pessoa;
    }

    public List<Pessoa> getAllPessoas() {
        List<Pessoa> lista = new ArrayList<Pessoa>();
        try {
            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            
            Criteria cri = session.createCriteria(Pessoa.class);
            cri.add(Restrictions.eq("ativo", true));
            lista = cri.list();
        } catch (Exception ex) {
        }
        return lista;
    }

    public Pessoa getPessoa(Integer id) {
        Pessoa dep = new Pessoa();
        try {

            SessionFactoryUtil sessionFactoryUtil = new SessionFactoryUtil();
            session = sessionFactoryUtil.getInstance().openSession();
            dep = (Pessoa) session.get(Pessoa.class, id);

        } catch (Exception ex) {
        }
        return dep;
    }

}

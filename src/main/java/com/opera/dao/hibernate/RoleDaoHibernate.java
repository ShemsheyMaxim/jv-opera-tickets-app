package com.opera.dao.hibernate;

import com.opera.dao.RoleDao;
import com.opera.exception.DataProcessingException;
import com.opera.model.Role;
import com.opera.model.RoleType;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoHibernate implements RoleDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoleDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Role role) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert role "
                    + role + " to table.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Role> getRoleByName(String roleType) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> roleByNameQuery = session.createQuery("SELECT r FROM Role r "
                    + "WHERE r.roleType = :roleType", Role.class);
            roleByNameQuery.setParameter("roleType", RoleType.valueOf(roleType));
            return roleByNameQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find role by name "
                    + roleType + " in table.", e);
        }
    }
}

package com.cinema.dao.hibernate;

import com.cinema.dao.CinemaHallDao;
import com.cinema.exception.DataProcessingException;
import com.cinema.model.CinemaHall;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CinemaHallDaoHibernate implements CinemaHallDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public CinemaHallDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(cinemaHall);
            transaction.commit();
            return cinemaHall;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert cinemaHall "
                    + cinemaHall + " to table. ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<CinemaHall> getAllCinemaHalls =
                    session.createQuery("SELECT ch FROM CinemaHall ch", CinemaHall.class);
            return getAllCinemaHalls.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Error retrieving all cinemaHalls. ", e);
        }
    }

    @Override
    public Optional<CinemaHall> get(Long cinemaHallId) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(CinemaHall.class, cinemaHallId));
        } catch (Exception e) {
            throw new DataProcessingException("Can't find cinema hall for id "
                    + cinemaHallId, e);
        }
    }
}

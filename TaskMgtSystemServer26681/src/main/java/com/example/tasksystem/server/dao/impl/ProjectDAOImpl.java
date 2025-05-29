package com.example.tasksystem.server.dao.impl;

import com.example.tasksystem.server.dao.ProjectDAO;
import com.example.tasksystem.server.entities.Project;
import com.example.tasksystem.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
// import org.hibernate.query.Query; // Not strictly needed for these methods but good for consistency

import java.util.List;
import java.util.Optional;

public class ProjectDAOImpl implements ProjectDAO {

    @Override
    public void save(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(project);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Project project = session.get(Project.class, id);
            return Optional.ofNullable(project);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Project> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    @Override
    public void update(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(project);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(project);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}

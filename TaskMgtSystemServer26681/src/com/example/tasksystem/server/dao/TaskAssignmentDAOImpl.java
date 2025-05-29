package com.example.tasksystem.server.dao;

import com.example.tasksystem.server.config.HibernateUtil;
import com.example.tasksystem.server.model.TaskAssignment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskAssignmentDAOImpl implements TaskAssignmentDAO {

    @Override
    public void save(TaskAssignment taskAssignment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(taskAssignment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<TaskAssignment> findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(TaskAssignment.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<TaskAssignment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TaskAssignment", TaskAssignment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<TaskAssignment> findByAppUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<TaskAssignment> query = session.createQuery(
                "FROM TaskAssignment ta WHERE ta.assignedAppUser.userId = :userId", TaskAssignment.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<TaskAssignment> findByTaskId(UUID taskId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<TaskAssignment> query = session.createQuery(
                "FROM TaskAssignment ta WHERE ta.task.taskId = :taskId", TaskAssignment.class);
            query.setParameter("taskId", taskId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<TaskAssignment> findByAssignedByAdminId(Integer adminId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<TaskAssignment> query = session.createQuery(
                "FROM TaskAssignment ta WHERE ta.assignedByAdmin.adminId = :adminId", TaskAssignment.class);
            query.setParameter("adminId", adminId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void update(TaskAssignment taskAssignment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(taskAssignment); // Or session.merge()
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(TaskAssignment taskAssignment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(taskAssignment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}

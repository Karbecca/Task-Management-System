package com.example.tasksystem.server.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml 
            // (expects hibernate.cfg.xml to be in the root of the classpath).
            return new Configuration().configure().buildSessionFactory(); 
        } catch (Throwable ex) {
            // Log the exception to help diagnose issues
            System.err.println("Initial SessionFactory creation failed." + ex);
            // Consider a more robust logging mechanism in a production application
            ex.printStackTrace(System.err); 
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // This should ideally not happen if the static initializer worked,
            // but as a safeguard or if used in a different context.
            throw new IllegalStateException("SessionFactory not initialized. Check Hibernate configuration and logs.");
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("Closing Hibernate SessionFactory...");
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed.");
        }
    }
}

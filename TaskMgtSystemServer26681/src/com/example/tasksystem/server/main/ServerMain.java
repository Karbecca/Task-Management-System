package com.example.tasksystem.server.main;

import com.example.tasksystem.server.config.HibernateUtil;
import com.example.tasksystem.server.rmi.interfaces.TaskService; // Good practice to use interface type
import com.example.tasksystem.server.rmi.services.TaskServiceImpl; // Corrected package for new service impl

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
// Naming can also be used: import java.rmi.Naming;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("Starting Task Management System Server (New Entity Structure)...");

        // 1. Initialize Hibernate
        try {
            // Eagerly initialize Hibernate to catch configuration errors early
            HibernateUtil.getSessionFactory(); 
            System.out.println("Hibernate SessionFactory initialized successfully.");
        } catch (Throwable ex) { // Catch Throwable for ExceptionInInitializerError
            System.err.println("FATAL: Initial Hibernate SessionFactory creation failed. Server cannot start.");
            ex.printStackTrace(System.err);
            System.exit(1); // Exit if Hibernate fails to initialize
        }

        // 2. Register Shutdown Hook for graceful Hibernate shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Task Management System Server...");
            HibernateUtil.shutdown();
            System.out.println("Hibernate SessionFactory closed. Server shutdown complete.");
        }));

        // 3. RMI Setup
        int port = 5000; // Default RMI port
        String serviceName = "TaskService"; // Service name in RMI registry

        try {
            System.out.println(String.format("Attempting to start RMI registry on port %d...", port));
            
            // Instantiate the service implementation from the correct package
            TaskService taskService = new TaskServiceImpl();

            // Create or get the RMI registry on the specified port
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port);
                System.out.println(String.format("RMI registry created on port %d.", port));
            } catch (RemoteException e) {
                // If createRegistry fails, it might mean the registry already exists.
                // Try to get the existing registry.
                System.out.println(String.format(
                    "RMI registry on port %d could not be created (it may already exist). Attempting to get existing registry...", port));
                registry = LocateRegistry.getRegistry(port); // Throws exception if not found
                System.out.println(String.format("Successfully obtained RMI registry on port %d.", port));
            }
            
            // Bind (or rebind) the service instance to the name in the registry.
            // Rebind is often preferred as it will replace any existing binding with the same name.
            registry.rebind(serviceName, taskService);
            // Alternative using Naming:
            // String rmiUrl = String.format("//localhost:%d/%s", port, serviceName);
            // Naming.rebind(rmiUrl, taskService);

            System.out.println(String.format("Service '%s' is bound and ready on port %d.", serviceName, port));
            System.out.println("Task Management System Server is running and awaiting client connections.");

        } catch (RemoteException e) {
            System.err.println("FATAL: RMI specific error during server startup: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("Server startup failed due to RMI error. Exiting.");
            System.exit(1); 
        } catch (Exception e) { // Catching other potential startup errors
            System.err.println("FATAL: A general error occurred during server startup: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("Server startup failed. Exiting.");
            System.exit(1);
        }
    }
}

package com.example.tasksystem.server.main;

import com.example.tasksystem.rmi.TaskService; // RMI Interface
import com.example.tasksystem.server.services.TaskServiceImpl;
import com.example.tasksystem.server.util.HibernateUtil;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Starting Task Management System Server...");

        // Initialize Hibernate SessionFactory
        try {
            // Call getSessionFactory to trigger initialization (and potential schema validation/update)
            HibernateUtil.getSessionFactory(); 
            System.out.println("Hibernate Initialized successfully.");
        } catch (Throwable ex) {
            System.err.println("Initial Hibernate SessionFactory creation failed: " + ex.getMessage());
            ex.printStackTrace();
            // Critical error, cannot proceed without Hibernate
            System.err.println("Server cannot start due to Hibernate initialization failure. Exiting.");
            System.exit(1); 
        }

        // Add shutdown hook for graceful Hibernate shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            HibernateUtil.shutdown();
            System.out.println("Hibernate SessionFactory closed. Server shutdown complete.");
        }));

        int port = 5000; // RMI Registry port
        String serviceName = "TaskService"; // Name for RMI binding

        try {
            System.out.println(String.format("Attempting to start RMI registry on port %d...", port));
            TaskService taskService = new TaskServiceImpl();
            
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(port);
                System.out.println(String.format("RMI registry created on port %d.", port));
            } catch (RemoteException e) {
                System.out.println(String.format("RMI registry on port %d already exists or could not be created. Attempting to get existing registry.", port));
                // If createRegistry fails, it might mean the registry already exists.
                // Try to get the existing registry.
                registry = LocateRegistry.getRegistry(port);
                 System.out.println(String.format("Successfully obtained existing RMI registry on port %d.", port));
            }
            
            // Using Naming.rebind for simplicity, which works with a local registry
            // The serviceURL format is //host:port/serviceName. If host is localhost, it can often be omitted.
            String rmiUrl = String.format("//localhost:%d/%s", port, serviceName);
            Naming.rebind(rmiUrl, taskService);
            // Alternatively, using registry.rebind directly:
            // registry.rebind(serviceName, taskService);


            System.out.println(String.format("Task Management System Server is running on port %d.", port));
            System.out.println(String.format("Service '%s' is bound as '%s' and ready for client connections.", serviceName, rmiUrl));
            System.out.println("Server setup complete. Waiting for client requests...");

        } catch (RemoteException e) {
            System.err.println("RMI specific server exception during startup: " + e.toString());
            e.printStackTrace();
            System.err.println("Server may not be able to accept RMI connections. Please check RMI configuration and network.");
            // Depending on the severity, you might want to exit
            // System.exit(1); 
        } catch (Exception e) {
            System.err.println("General error during server startup: " + e.toString());
            e.printStackTrace();
            System.err.println("Server failed to start. Exiting.");
            System.exit(1);
        }
    }
}

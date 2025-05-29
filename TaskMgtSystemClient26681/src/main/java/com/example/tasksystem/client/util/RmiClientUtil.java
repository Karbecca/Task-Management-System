package com.example.tasksystem.client.util;

import com.example.tasksystem.rmi.TaskService; // RMI interface

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiClientUtil {

    private static final String RMI_HOST = "localhost";
    private static final int RMI_PORT = 5000; // Ensure this matches the server's port
    private static final String SERVICE_NAME = "TaskService"; // Ensure this matches the server's service name

    private static TaskService taskService = null;

    private RmiClientUtil() {
        // Private constructor to prevent instantiation
    }

    public static TaskService getTaskService() {
        if (taskService == null) {
            try {
                String rmiUrl = String.format("rmi://%s:%d/%s", RMI_HOST, RMI_PORT, SERVICE_NAME);
                System.out.println("Connecting to RMI service at: " + rmiUrl); // For debugging
                taskService = (TaskService) Naming.lookup(rmiUrl);
                System.out.println("Successfully connected to RMI service.");
            } catch (NotBoundException e) {
                System.err.println("Service lookup failed: Service '" + SERVICE_NAME + "' not bound at " + RMI_HOST + ":" + RMI_PORT);
                System.err.println("Please ensure the server is running and the service is correctly bound.");
                // e.printStackTrace(); // Uncomment for detailed stack trace during development
                throw new RuntimeException("Service lookup failed: Service not bound. Check server status and RMI configuration.", e);
            } catch (MalformedURLException e) {
                System.err.println("Service lookup failed: Malformed RMI URL: " + e.getMessage());
                // e.printStackTrace();
                throw new RuntimeException("Service lookup failed: Malformed RMI URL. Contact support if this persists.", e);
            } catch (RemoteException e) {
                System.err.println("Service lookup failed: Remote exception. This often means the RMI server is not reachable or is having issues.");
                System.err.println("Details: " + e.getMessage());
                // e.printStackTrace();
                throw new RuntimeException("Service lookup failed: Remote exception. Ensure RMI server is running and accessible.", e);
            } catch (Exception e) {
                System.err.println("An unexpected error occurred during RMI service lookup: " + e.getMessage());
                // e.printStackTrace();
                throw new RuntimeException("Service lookup failed due to an unexpected error.", e);
            }
        }
        return taskService;
    }
    
    // Optional: A method to reset the service, e.g., for testing or forcing a reconnect.
    public static void resetService() {
        taskService = null;
        System.out.println("RMI TaskService stub has been reset. Next call to getTaskService() will perform a fresh lookup.");
    }
}

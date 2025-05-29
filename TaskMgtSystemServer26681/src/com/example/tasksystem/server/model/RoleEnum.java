package com.example.tasksystem.server.model;

import java.io.Serializable;

public enum RoleEnum implements Serializable { // Implementing Serializable for good measure, enums are serializable by default.
    SUPERADMIN,
    EDITOR,
    VIEWER;

    // Optional: Add methods if needed, e.g., a method to get a display-friendly name
    // public String getDisplayName() {
    //     switch (this) {
    //         case SUPERADMIN: return "Super Administrator";
    //         case EDITOR: return "Editor";
    //         case VIEWER: return "Viewer";
    //         default: return this.toString();
    //     }
    // }
}

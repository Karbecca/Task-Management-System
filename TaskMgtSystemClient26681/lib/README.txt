Required Libraries for TaskMgtSystemClient26681:
-------------------------------------------------

You will need to download the following JAR files and add them to your Java project's library path (e.g., in NetBeans IDE or any other IDE). You can typically download these from their official websites or from Maven Central Repository.

**1. Apache POI (for Excel Export)**

Apache POI is used for creating and manipulating Microsoft Office file formats, including Excel. For `.xlsx` support (OOXML), you'll need the main POI OOXML JAR and its dependencies.

*   **Core POI JARs:**
    *   `poi-ooxml-<version>.jar` (e.g., 5.2.5) - Handles .xlsx Excel format.
    *   `poi-<version>.jar` (e.g., 5.2.5) - Core POI library.
    *   `poi-ooxml-full-<version>.jar` (e.g., 5.2.5) - Sometimes available, bundles many dependencies. If using this, you might need fewer individual commons JARs. Check the POI version's specific requirements.

*   **Common Dependencies for Apache POI (versions may vary based on POI version):**
    *   `commons-codec-<version>.jar` (e.g., 1.15 or 1.16.0)
    *   `commons-collections4-<version>.jar` (e.g., 4.4)
    *   `commons-math3-<version>.jar` (e.g., 3.6.1) - Often for certain advanced features.
    *   `SparseBitSet-<version>.jar` (e.g., 1.2 or 1.3)
    *   `xmlbeans-<version>.jar` (e.g., `xmlbeans-5.2.0.jar` corresponding to POI 5.2.5, often named `ooxml-schemas` in older versions or `xmlbeans` directly) - Crucial for OOXML.
    *   `commons-compress-<version>.jar` (e.g., 1.21 or 1.26.0)
    *   `log4j-api-<version>.jar` (e.g., 2.17.2 or newer, if POI's logging facade requires it. POI uses commons-logging which can delegate.)

    **Recommendation for Apache POI:**
    The easiest way to get all necessary POI JARs is to download the binary distribution ZIP from the Apache POI website for your chosen version (e.g., 5.2.5). This distribution typically includes:
    *   `poi-VERSION.jar`
    *   `poi-ooxml-VERSION.jar`
    *   `poi-ooxml-full-VERSION.jar` (which bundles many dependencies)
    *   A `lib/` directory containing common dependencies like `commons-codec`, `commons-collections4`, `commons-compress`.
    *   An `ooxml-lib/` (or similar) directory containing OOXML specific dependencies like `xmlbeans`.
    Add all JARs from these locations.

**2. OpenPDF (for PDF Export)**

OpenPDF is a Java library for creating and editing PDF files, forked from iText (LGPL/MPL licensed).

*   **Core OpenPDF JAR:**
    *   `openpdf-<version>.jar` (e.g., 1.3.30 or a later compatible version)
    *   OpenPDF is generally quite self-contained. For basic PDF table creation and text, the core JAR is often sufficient. Check the specific version's documentation if you encounter ClassNotFoundException for any specific features.

**Note on Shared Code (RMI Interface and Entities):**

*   **RMI Interface (`TaskService.java`):** This interface definition is copied from the server project (or a shared location) and placed directly into the client's source code, typically in a package like `com.example.tasksystem.rmi`.
*   **Entity Classes (`User.java`, `Project.java`, `Task.java`):** These class definitions (which must be `Serializable`) are also copied from the server project (or a shared location) and placed directly into the client's source code, for example, in a package like `com.example.tasksystem.shared.entities`.

These shared files are compiled as part of the client project and are not external JARs that you need to place in the `lib` folder.

**Instructions for Adding External JARs (e.g., in NetBeans IDE):**

1.  Create a `lib` folder within your `TaskMgtSystemClient26681` project directory (e.g., at the same level as `src`).
2.  Download the required JAR files for Apache POI and OpenPDF.
3.  Place all these downloaded JAR files directly into the `lib` folder you created.
4.  In NetBeans:
    *   Right-click on your project name in the "Projects" window.
    *   Select "Properties".
    *   In the "Project Properties" dialog, go to the "Libraries" category.
    *   Ensure the "Compile" tab is selected (or "Classpath" in some versions).
    *   Click the "Add JAR/Folder..." button.
    *   Navigate to your project's `lib` folder.
    *   Select all the JAR files within the `lib` folder.
    *   Click "Open". The JARs should now be listed under "Compile-time Libraries".
    *   Click "OK".

This will add the external libraries to your project's classpath, allowing you to use their functionalities (like Excel and PDF export) in your client application. Always ensure that the versions of the libraries you download are compatible with your Java version (e.g., Java 8).

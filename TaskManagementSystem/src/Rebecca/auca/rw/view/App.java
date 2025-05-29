/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Christian.auca.rw.view;



import javax.swing.JOptionPane;
import Christian.auca.rw.dao.AdminDao;
import Christian.auca.rw.model.Admin;

public class App extends javax.swing.JFrame {

    public App() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            AdminDao adminDao = new AdminDao();
            int adminCount = adminDao.getAdminCount(); // Assuming a method to get the admin count
            if (adminCount <= 0) {
                JOptionPane.showMessageDialog(null, "No Admin Registered");
                int choice = JOptionPane.showConfirmDialog(null, "Create a Super Admin Account First. Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                   SuperAdminSignUpPage superAdminSignUp = new SuperAdminSignUpPage();
                    superAdminSignUp.setVisible(true);
                } else {
                    System.exit(0);
                }
            } else {
                Splash splash = new Splash();
                splash.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}


package ui;

import javax.swing.SwingUtilities;

public class ClickpayApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}

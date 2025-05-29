package Christian.auca.rw.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {
    private Color checkColor = new Color(7, 164, 121); // Default color for the checkmark
    
    public CheckBox() {
        super();
    }

    public Color getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(Color checkColor) {
        this.checkColor = checkColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        if (isSelected()) {
            Graphics2D g2d = (Graphics2D) grphcs.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2d.setColor(checkColor);
//            int size = Math.min(getWidth(), getHeight()) - 4;
//            int startX = (getWidth() - size) / 2;
//            int startY = (getHeight() - size) / 2;
//            g2d.fillRect(startX, startY, size, size);
            g2d.dispose();
        }
    }
}

package client;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom button with rounded corners and hover effects
 */
public class RoundedButton extends JButton {
    private static final long serialVersionUID = 1L;
    
    // Colors from design spec
    private static final Color BUTTON_COLOR = new Color(0, 123, 255); // #007BFF
    private static final Color BUTTON_HOVER_COLOR = new Color(0, 86, 179); // #0056B3
    private static final Color TEXT_COLOR = Color.WHITE;
    
    private boolean isHovered = false;
    
    public RoundedButton(String text) {
        super(text);
        
        // Set button appearance
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(TEXT_COLOR);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorder(new EmptyBorder(8, 15, 8, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set button color based on state
        if (isEnabled()) {
            g2d.setColor(isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
        } else {
            g2d.setColor(new Color(200, 200, 200)); // Disabled color
        }
        
        // Create rounded rectangle shape
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        g2d.fill(roundedRectangle);
        
        g2d.dispose();
        
        // Paint the text
        super.paintComponent(g);
    }
}

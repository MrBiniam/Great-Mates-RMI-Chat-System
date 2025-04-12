package client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom panel with gradient background for the chat application header
 * Includes the Dilla University logo and the app title
 */
public class GradientHeaderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Colors from design spec
    private static final Color GRADIENT_START = new Color(0, 123, 255); // #007BFF
    private static final Color GRADIENT_END = new Color(0, 191, 255); // #00BFFF
    
    private ImageIcon logoIcon;
    
    public GradientHeaderPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(new Dimension(0, 70)); // Fixed height for header
        
        // Try to load the Dilla University logo
        try {
            BufferedImage logoImage = ImageIO.read(new File("../Images/dilla university.jfif"));
            Image scaledLogo = logoImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledLogo);
        } catch (IOException e) {
            // Use a placeholder if image can't be loaded
            logoIcon = null;
        }
        
        // Create the logo label
        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("DU");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            logoLabel.setForeground(Color.WHITE);
        }
        
        // Create the title label
        JLabel titleLabel = new JLabel("Great Mates Chat", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Add components to panel
        add(logoLabel, BorderLayout.WEST);
        add(titleLabel, BorderLayout.CENTER);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, GRADIENT_START,
                getWidth(), 0, GRADIENT_END);
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.dispose();
    }
}

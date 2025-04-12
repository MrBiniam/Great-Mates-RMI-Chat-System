package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Splash screen for the chat application
 * Displays a welcome message with the Great Mates image
 */
public class SplashScreen extends JWindow {
    private static final long serialVersionUID = 1L;
    private static final int DISPLAY_TIME = 3000; // 3 seconds
    
    public SplashScreen() {
        initComponents();
    }
    
    private void initComponents() {
        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        try {
            // Load the Great Mates image
            BufferedImage backgroundImage = ImageIO.read(new File("../Images/Great Mates.jpg"));
            
            // Scale image to fit the splash screen
            Image scaledImage = backgroundImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
            
            // Create a label with the background image
            JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
            backgroundLabel.setLayout(new BorderLayout());
            
            // Create a semi-transparent panel for the text
            JPanel textPanel = new JPanel() {
                private static final long serialVersionUID = 1L;
                
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            
            textPanel.setOpaque(false);
            textPanel.setLayout(new BorderLayout());
            
            // Create welcome text with a modern font
            JLabel welcomeLabel = new JLabel("Welcome to Our Chat System", JLabel.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            welcomeLabel.setForeground(Color.WHITE);
            
            // Add a subtitle
            JLabel subtitleLabel = new JLabel("Great Mates Chat", JLabel.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitleLabel.setForeground(Color.WHITE);
            
            // Add text to the panel
            textPanel.add(welcomeLabel, BorderLayout.CENTER);
            textPanel.add(subtitleLabel, BorderLayout.SOUTH);
            
            // Add the text panel to the background
            backgroundLabel.add(textPanel, BorderLayout.CENTER);
            
            // Add background to the main panel
            mainPanel.add(backgroundLabel, BorderLayout.CENTER);
            
        } catch (IOException e) {
            // If image loading fails, create a simple gradient background
            JPanel gradientPanel = new JPanel() {
                private static final long serialVersionUID = 1L;
                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Create a gradient paint from blue to lighter blue
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(0, 123, 255), 
                            0, getHeight(), new Color(0, 191, 255));
                    
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            
            gradientPanel.setPreferredSize(new Dimension(500, 300));
            
            // Create welcome text
            JLabel welcomeLabel = new JLabel("Welcome to Our Chat System", JLabel.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            welcomeLabel.setForeground(Color.WHITE);
            
            // Add to the panel
            gradientPanel.setLayout(new BorderLayout());
            gradientPanel.add(welcomeLabel, BorderLayout.CENTER);
            
            mainPanel.add(gradientPanel, BorderLayout.CENTER);
        }
        
        // Add the main panel to the window
        getContentPane().add(mainPanel);
        
        // Center on screen
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Display the splash screen for the specified duration
     * then launch the main application
     */
    public void showSplash() {
        setVisible(true);
        
        // Set a timer to close the splash and start the main app
        Timer timer = new Timer(DISPLAY_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSplash();
                new ClientRMIGUI();
            }
        });
        
        timer.setRepeats(false);
        timer.start();
    }
    
    private void closeSplash() {
        setVisible(false);
        dispose();
    }
    
    /**
     * Launch the application with the splash screen
     */
    public static void main(String[] args) {
        try {
            // Set Nimbus look and feel if available
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Use default look and feel if Nimbus is not available
        }
        
        // Create and display splash screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SplashScreen splash = new SplashScreen();
                splash.showSplash();
            }
        });
    }
}

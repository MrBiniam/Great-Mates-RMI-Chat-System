package client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom renderer for displaying users in the user list
 * with avatars and hover effect
 */
public class UserListRenderer extends JPanel implements ListCellRenderer<String> {
    private static final long serialVersionUID = 1L;
    
    private JLabel usernameLabel;
    private JLabel avatarLabel;
    private String username;
    private boolean isSelected;
    
    // Cache for user avatars (username -> avatar index)
    private static final Map<String, Integer> userAvatars = new HashMap<>();
    private static final Random random = new Random();
    
    // Cache for loaded avatar images
    private static final Map<Integer, ImageIcon> avatarImages = new HashMap<>();
    
    // Colors from design spec
    private static final Color SELECTED_COLOR = new Color(232, 240, 254); // #E8F0FE - light blue
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // #2C3E50 - dark gray-blue
    
    public UserListRenderer() {
        setLayout(new BorderLayout(10, 0));
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setOpaque(true);
        
        // Avatar label
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(32, 32));
        
        // Username label
        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        
        // Add components to panel
        add(avatarLabel, BorderLayout.WEST);
        add(usernameLabel, BorderLayout.CENTER);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String username,
            int index, boolean isSelected, boolean cellHasFocus) {
        
        this.username = username;
        this.isSelected = isSelected;
        
        // Set username text
        usernameLabel.setText(username);
        
        // Set avatar image
        int avatarIndex = getAvatarIndexForUser(username);
        ImageIcon avatar = getAvatarImage(avatarIndex);
        if (avatar != null) {
            avatarLabel.setIcon(avatar);
        }
        
        // Set background color based on selection state
        if (isSelected) {
            setBackground(SELECTED_COLOR);
        } else {
            setBackground(Color.WHITE);
        }
        
        return this;
    }
    
    /**
     * Get or assign an avatar index for a given username
     * 
     * @param username The username
     * @return An index (1-6) for the user's avatar
     */
    private int getAvatarIndexForUser(String username) {
        if (!userAvatars.containsKey(username)) {
            // Assign a random avatar (1-6) to this user
            int avatarIndex = random.nextInt(6) + 1;
            userAvatars.put(username, avatarIndex);
        }
        return userAvatars.get(username);
    }
    
    /**
     * Load and cache avatar images
     * 
     * @param index The avatar index (1-6)
     * @return The avatar image icon, or null if it couldn't be loaded
     */
    private ImageIcon getAvatarImage(int index) {
        if (avatarImages.containsKey(index)) {
            return avatarImages.get(index);
        }
        
        try {
            // Check what file extension exists
            String[] extensions = {".jpg", ".jfif", ".png"};
            String filename = null;
            
            for (String ext : extensions) {
                File file = new File("../Images/avatar" + index + ext);
                if (file.exists()) {
                    filename = file.getPath();
                    break;
                }
            }
            
            if (filename == null) {
                return null;
            }
            
            // Load and scale the avatar image
            BufferedImage img = ImageIO.read(new File(filename));
            Image scaledImg = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImg);
            
            // Cache the image
            avatarImages.put(index, icon);
            return icon;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw a subtle separator line at the bottom
        g.setColor(new Color(240, 240, 240));
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}

package client;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom renderer for chat messages to display them as bubbles
 * with different styles for sent and received messages
 */
public class ChatBubbleRenderer extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private String message;
    private String sender;
    private boolean isOwnMessage;
    private Color bubbleColor;
    private Color textColor;
    private Font messageFont;
    private Font senderFont;
    
    // Colors from design spec
    private static final Color SENT_BUBBLE_COLOR = new Color(209, 242, 235); // #D1F2EB - mint green
    private static final Color RECEIVED_BUBBLE_COLOR = Color.WHITE;
    private static final Color RECEIVED_BORDER_COLOR = new Color(214, 219, 223); // #D6DBDF
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // #2C3E50 - dark gray-blue
    
    public ChatBubbleRenderer(String message, String sender, boolean isOwnMessage) {
        this.message = message;
        this.sender = sender;
        this.isOwnMessage = isOwnMessage;
        this.bubbleColor = isOwnMessage ? SENT_BUBBLE_COLOR : RECEIVED_BUBBLE_COLOR;
        this.textColor = TEXT_COLOR;
        this.messageFont = new Font("Segoe UI", Font.PLAIN, 13);
        this.senderFont = new Font("Segoe UI", Font.BOLD, 13);
        
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setLayout(new BorderLayout(5, 5));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Calculate bubble dimensions
        int width = getWidth() - 20;
        int height = getHeight() - 10;
        int x = isOwnMessage ? getWidth() - width - 10 : 10;
        int y = 5;
        int arcSize = 15; // Rounded corner size
        
        // Create a rounded rectangle for the bubble
        RoundRectangle2D bubble = new RoundRectangle2D.Float(
                x, y, width, height, arcSize, arcSize);
        
        // Fill the bubble
        g2d.setColor(bubbleColor);
        g2d.fill(bubble);
        
        // Draw border for received messages
        if (!isOwnMessage) {
            g2d.setColor(RECEIVED_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(1f));
            g2d.draw(bubble);
        }
        
        g2d.dispose();
    }
    
    /**
     * Creates a chat bubble panel for the given message
     * 
     * @param message The message text
     * @param sender The name of the sender
     * @param isOwnMessage Whether this message was sent by the current user
     * @return A panel containing the formatted message
     */
    public static JPanel createBubble(String message, String sender, boolean isOwnMessage) {
        JPanel bubblePanel = new JPanel(new BorderLayout(5, 5));
        bubblePanel.setOpaque(false);
        
        // Create sender label
        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        senderLabel.setForeground(TEXT_COLOR);
        
        // Create message label with word wrap
        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setBorder(null);
        
        // Create bubble with sender and message
        JPanel contentPanel = new JPanel(new BorderLayout(0, 3));
        contentPanel.setOpaque(false);
        contentPanel.add(senderLabel, BorderLayout.NORTH);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        // Create timestamp (just for UI example - would need real implementation)
        JLabel timeLabel = new JLabel("now");
        timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timeLabel.setForeground(new Color(150, 150, 150));
        
        // Create outer bubble panel
        ChatBubbleRenderer bubble = new ChatBubbleRenderer(message, sender, isOwnMessage);
        bubble.setLayout(new BorderLayout(5, 5));
        bubble.add(contentPanel, BorderLayout.CENTER);
        bubble.add(timeLabel, BorderLayout.SOUTH);
        
        // Add bubble to main panel with proper alignment
        if (isOwnMessage) {
            bubblePanel.add(Box.createHorizontalStrut(80), BorderLayout.WEST);
            bubblePanel.add(bubble, BorderLayout.CENTER);
        } else {
            bubblePanel.add(bubble, BorderLayout.CENTER);
            bubblePanel.add(Box.createHorizontalStrut(80), BorderLayout.EAST);
        }
        
        return bubblePanel;
    }
}

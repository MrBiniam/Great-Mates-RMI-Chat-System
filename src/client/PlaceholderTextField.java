package client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Custom text field with placeholder text
 */
public class PlaceholderTextField extends JTextField {
    private static final long serialVersionUID = 1L;
    
    private String placeholder;
    private boolean showingPlaceholder;
    
    // Custom colors
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // #2C3E50
    
    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        
        // Set appearance
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 15, 10, 15)));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        
        // Add focus listeners to handle placeholder
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(TEXT_COLOR);
                    showingPlaceholder = false;
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (getText().isEmpty()) {
                    setPlaceholder();
                }
            }
        });
        
        // Initialize with placeholder
        setPlaceholder();
    }
    
    private void setPlaceholder() {
        setText(placeholder);
        setForeground(PLACEHOLDER_COLOR);
        showingPlaceholder = true;
    }
    
    @Override
    public String getText() {
        return showingPlaceholder ? "" : super.getText();
    }
}

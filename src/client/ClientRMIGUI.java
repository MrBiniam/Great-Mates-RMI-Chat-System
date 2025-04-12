package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author Daragh Walshe 	B00064428
 * RMI Assignment 2		 	April 2015
 * Enhanced UI by Great Mates Team
 *
 */
public class ClientRMIGUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;	
	private JPanel textPanel, inputPanel, broadcastPanel, privateMessagesPanel;
	private JTextField textField;
	private String name, message;
	private Font segoeFont = new Font("Segoe UI", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);//top,r,b,l
	private ChatClient3 chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    
    // UI colors from design spec
    private static final Color BG_COLOR = new Color(244, 246, 249); // #F4F6F9
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // #2C3E50
    
    // New colors for message panels
    private static final Color BROADCAST_BG_COLOR = new Color(232, 248, 245); // #E8F8F5 light turquoise
    private static final Color BROADCAST_BORDER_COLOR = new Color(163, 228, 215); // #A2D9CE
    private static final Color BROADCAST_TEXT_COLOR = new Color(44, 62, 80); // #154360 deep blue-gray
    private static final Color BROADCAST_SEPARATOR_COLOR = new Color(213, 245, 227); // #D0ECE7
    
    private static final Color PRIVATE_BG_COLOR = new Color(253, 237, 236); // Light Rose - deprecated
    private static final Color PRIVATE_BORDER_COLOR = new Color(218, 165, 32); // Golden
    private static final Color PRIVATE_TEXT_COLOR = new Color(0, 0, 0); // Black
    private static final Color PRIVATE_BG_GOLDEN = new Color(253, 235, 170); // Golden background
    
    protected JPanel textArea; // Changed from JTextArea to JPanel for custom bubble rendering
    protected JFrame frame;
    protected RoundedButton privateMsgButton, startButton, sendButton;
    protected JPanel clientPanel, userPanel;

	/**
	 * Main method to start client GUI app.
	 * @param args
	 */
	public static void main(String args[]){
		//set the look and feel to 'Nimbus'
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch(Exception e){
			}
		// Show splash screen first instead of directly creating ClientRMIGUI
		SplashScreen splash = new SplashScreen();
		splash.showSplash();
		}//end main
	
	
	/**
	 * GUI Constructor
	 */
	public ClientRMIGUI(){
		// Set a more elegant title for the chat client
		frame = new JFrame("Great Mates Chat");	
	
		//-----------------------------------------
		/*
		 * intercept close method, inform server we are leaving
		 * then let the system exit.
		 */
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		    	if(chatClient != null){
			    	try {
			        	sendMessage("Bye all, I am leaving");
			        	chatClient.serverIF.leaveChat(name);
					} catch (RemoteException e) {
						e.printStackTrace();
					}		        	
		        }
		        System.exit(0);  
		    }   
		});
		
		// Set up the main container with a border layout
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.setBackground(BG_COLOR);
		
		// Add the header panel at the top
		GradientHeaderPanel headerPanel = new GradientHeaderPanel();
		c.add(headerPanel, BorderLayout.NORTH);
		
		// Main panel with chat area and input field
		JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
		mainPanel.setBackground(BG_COLOR);
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		// Add the chat display panel
		mainPanel.add(getTextPanel(), BorderLayout.CENTER);
		
		// Add the input panel at the bottom of the main panel
		mainPanel.add(getInputPanel(), BorderLayout.SOUTH);
		
		// Add the main panel to the center of the container
		c.add(mainPanel, BorderLayout.CENTER);
		
		// Add the users panel to the left side
		c.add(getUsersPanel(), BorderLayout.WEST);

		// Set up the frame with the content
		frame.add(c);
		frame.setSize(800, 600); // Set a standard size
		frame.setLocationRelativeTo(null); // Center on screen
		textField.requestFocus();
	
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	/**
	 * Method to set up the JPanel to display the chat text
	 * @return
	 */
	public JPanel getTextPanel(){
		// Create main panel
		textPanel = new JPanel(new BorderLayout(0, 10));
		textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create a vertically split panel for broadcast messages and private messages
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(350); // Adjust based on preferred sizing
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		
		// Create broadcast messages panel (top)
		broadcastPanel = createMessagePanel(
			"Broadcast Messages", 
			BROADCAST_BG_COLOR, 
			BROADCAST_BORDER_COLOR,
			BROADCAST_TEXT_COLOR
		);
		
		// Create panel for private messages (bottom)
		privateMessagesPanel = new JPanel();
		privateMessagesPanel.setLayout(new BoxLayout(privateMessagesPanel, BoxLayout.Y_AXIS));
		privateMessagesPanel.setBackground(BG_COLOR);
		privateMessagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		// Create title for private messages section
		JLabel privateTitle = new JLabel("Private Messages");
		privateTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		privateTitle.setForeground(PRIVATE_TEXT_COLOR);
		privateTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
		privateTitle.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a container panel for private messages with title
		JPanel privatePanelWithTitle = new JPanel(new BorderLayout());
		privatePanelWithTitle.setBackground(BG_COLOR);
		privatePanelWithTitle.add(privateTitle, BorderLayout.NORTH);
		privatePanelWithTitle.add(privateMessagesPanel, BorderLayout.CENTER);
		
		// Add panels to split pane
		splitPane.setTopComponent(new JScrollPane(broadcastPanel));
		splitPane.setBottomComponent(new JScrollPane(privatePanelWithTitle));
		
		// Add split pane to main panel
		textPanel.add(splitPane, BorderLayout.CENTER);
		
		return textPanel;
	}
	
	/**
	 * Create a styled message panel with title
	 * @param title Panel title
	 * @param bgColor Background color
	 * @param borderColor Border color
	 * @param textColor Text color
	 * @return Styled panel
	 */
	private JPanel createMessagePanel(String title, Color bgColor, Color borderColor, Color textColor) {
		// Create outer panel with border layout
		JPanel outerPanel = new JPanel(new BorderLayout());
		outerPanel.setBackground(bgColor);
		outerPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(borderColor, 1, true),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));
		
		// Create title label
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		titleLabel.setForeground(textColor);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
		
		// Create panel for messages with vertical BoxLayout
		JPanel messagesPanel = new JPanel();
		messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
		messagesPanel.setBackground(bgColor);
		
		// Add vertical glue to push messages to the top
		messagesPanel.add(Box.createVerticalGlue());
		
		// Add components to outer panel
		outerPanel.add(titleLabel, BorderLayout.NORTH);
		outerPanel.add(messagesPanel, BorderLayout.CENTER);
		
		return messagesPanel;
	}
	
	/**
	 * Method to build the panel with input field
	 * @return inputPanel
	 */
	public JPanel getInputPanel(){
		// Create panel with BorderLayout
		inputPanel = new JPanel(new BorderLayout(10, 0));
		inputPanel.setBackground(BG_COLOR);
		inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		// Create text field (using standard JTextField instead of PlaceholderTextField)
		textField = new JTextField("Type your message...");
		textField.setFont(segoeFont);
		textField.setForeground(Color.GRAY);
		textField.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
			new EmptyBorder(10, 15, 10, 15)));
		
		// Add focus listeners for placeholder behavior
		textField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (textField.getText().equals("Type your message...")) {
					textField.setText("");
					textField.setForeground(TEXT_COLOR);
				}
			}
			
			@Override
			public void focusLost(java.awt.event.FocusEvent evt) {
				if (textField.getText().isEmpty()) {
					textField.setText("Type your message...");
					textField.setForeground(Color.GRAY);
				}
			}
		});
		
		// Add document listener to enable/disable send button based on text content
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSendButtonState();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSendButtonState();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSendButtonState();
			}
			
			private void updateSendButtonState() {
				// Only enable the send button if connected and text field is not empty
				if (!startButton.isEnabled() && sendButton != null) {
					String text = textField.getText();
					boolean isEmpty = text.isEmpty() || text.equals("Type your message...");
					sendButton.setEnabled(!isEmpty);
				}
			}
		});
		
		// Create a styled send button
		sendButton = new RoundedButton("Send");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);
		
		// Add components to panel
		inputPanel.add(textField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		
		return inputPanel;
	}

	/**
	 * Method to build the panel displaying currently connected users
	 * with a call to the button panel building method
	 * @return
	 */
	public JPanel getUsersPanel(){
		// Create main user panel with border layout
		userPanel = new JPanel(new BorderLayout());
		userPanel.setBackground(Color.WHITE);
		userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));
		userPanel.setPreferredSize(new Dimension(200, 0));
		
		// Create header for user list
		JPanel userHeaderPanel = new JPanel(new BorderLayout());
		userHeaderPanel.setBackground(new Color(250, 250, 250));
		userHeaderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
		userHeaderPanel.setPreferredSize(new Dimension(0, 40));
		
		JLabel userLabel = new JLabel("  Current Users", JLabel.LEFT);
		userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		userLabel.setForeground(TEXT_COLOR);
		
		userHeaderPanel.add(userLabel, BorderLayout.CENTER);
		userPanel.add(userHeaderPanel, BorderLayout.NORTH);
		
		// Initialize with "No other users" message
		String[] noClientsYet = {"No other users"};
		setClientPanel(noClientsYet);
		
		// Add the button panel at the bottom
		userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
		
		return userPanel;		
	}

	/**
	 * Populate current user panel with a 
	 * selectable list of currently connected users
	 * @param currClients
	 */
    public void setClientPanel(String[] currClients) {  	
    	// Create client panel for user list
    	clientPanel = new JPanel(new BorderLayout());
    	clientPanel.setBackground(Color.WHITE);
    	
        // Create list model and add users
        listModel = new DefaultListModel<String>();
        for(String s : currClients){
        	listModel.addElement(s);
        }
        
        // Enable/disable private message button
        if(currClients.length > 1 && privateMsgButton != null){
        	privateMsgButton.setEnabled(true);
        } else if (privateMsgButton != null) {
        	privateMsgButton.setEnabled(false);
        }
        
        // Create user list with custom renderer
        list = new JList<String>(listModel);
        list.setCellRenderer(new UserListRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(segoeFont);
        
        // Add the list to a scroll pane
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setBorder(BorderFactory.createEmptyBorder());

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }
	
	/**
	 * Make the buttons and add the listener
	 * @return
	 */
	public JPanel makeButtonPanel() {		
		// Create a panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Create custom styled buttons (but NOT sendButton - it's already created in getInputPanel)
		privateMsgButton = new RoundedButton("Send PM");
		privateMsgButton.addActionListener(this);
		privateMsgButton.setEnabled(false);
		privateMsgButton.setAlignmentX(CENTER_ALIGNMENT);
		
		startButton = new RoundedButton("Start");
		startButton.addActionListener(this);
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		
		// Add buttons to panel with spacing
		buttonPanel.add(privateMsgButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(startButton);
		
		return buttonPanel;
	}
	
	/**
	 * Action handling on the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e){

		try {
			//get connected to chat service
			if(e.getSource() == startButton){
				name = textField.getText();	
				// Don't connect if the text is just the placeholder
				if(name.length() != 0 && !name.equals("Type your message...")){
					frame.setTitle(name + " - Great Mates Chat");
					textField.setText("Type your message...");
					textField.setForeground(Color.GRAY);
					
					// Clear the message panels
					broadcastPanel.removeAll();
					privateMessagesPanel.removeAll();
					
					// Add a system message about connecting
					addSystemMessage("Connecting to chat as '" + name + "'...");
					
					// Get connected to the chat server
					getConnected(name);
					
					if(!chatClient.connectionProblem){
						startButton.setEnabled(false);
						sendButton.setEnabled(false); // Disabled until text is entered
					}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Enter your name to Start", "Empty Name", JOptionPane.WARNING_MESSAGE);
				}
			}

			//get text and clear textField
			if(e.getSource() == sendButton){
				message = textField.getText();
				if (!message.trim().isEmpty() && !message.equals("Type your message...")) {
					textField.setText("Type your message...");
					textField.setForeground(Color.GRAY);
					sendMessage(message);
				}
			}
			
			//send a private message, to selected users
			if(e.getSource() == privateMsgButton){
				int[] privateList = list.getSelectedIndices();
				
				if (privateList.length > 0) {
					message = textField.getText();
					if (!message.trim().isEmpty() && !message.equals("Type your message...")) {
						textField.setText("Type your message...");
						textField.setForeground(Color.GRAY);
						sendPrivate(privateList);
					}
				} else {
					JOptionPane.showMessageDialog(frame, 
					    "Please select at least one user to send a private message.",
					    "No Users Selected", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		}
		catch (RemoteException remoteExc) {			
			remoteExc.printStackTrace();	
		}
		
	}//end actionPerformed

	// --------------------------------------------------------------------
	
	/**
	 * Add a system message in italics
	 * @param systemMessage
	 */
	private void addSystemMessage(String systemMessage) {
		// Create system message panel
		JPanel systemPanel = new JPanel(new BorderLayout());
		systemPanel.setOpaque(false);
		systemPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
		
		JLabel systemLabel = new JLabel(systemMessage, JLabel.CENTER);
		systemLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		systemLabel.setForeground(new Color(100, 100, 100));
		
		systemPanel.add(systemLabel, BorderLayout.CENTER);
		
		// Add only to the broadcast panel
		addPanelToMessageArea(systemPanel, broadcastPanel);
	}
	
	/**
	 * Add a chat message to the appropriate panel
	 * @param sender The sender's name
	 * @param messageText The message text
	 * @param isPrivate Whether this is a private message
	 */
	private void addChatMessage(String sender, String messageText, boolean isPrivate) {
		addChatMessage(sender, messageText, isPrivate, null);
	}
	
	/**
	 * Add a chat message to the appropriate panel with recipient information
	 * @param sender The sender's name
	 * @param messageText The message text
	 * @param isPrivate Whether this is a private message
	 * @param recipients Optional recipient names for outgoing private messages
	 */
	private void addChatMessage(String sender, String messageText, boolean isPrivate, String recipients) {
		// Create message bubble
		boolean isOwnMessage = sender.equals(name);
		JPanel bubblePanel;
		
		if (isPrivate) {
			// For private messages, create a distinctive golden individual box
			JPanel privateMessagePanel = new JPanel(new BorderLayout(10, 0));
			privateMessagePanel.setBackground(PRIVATE_BG_GOLDEN); // Golden for private messages
			privateMessagePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(PRIVATE_BORDER_COLOR, 1, true),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)
			));
			privateMessagePanel.setOpaque(true);
			privateMessagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
			privateMessagePanel.setAlignmentX(LEFT_ALIGNMENT);
			
			// Add PM icon for visual clarity
			JLabel iconLabel = new JLabel("PM");
			iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
			iconLabel.setForeground(PRIVATE_TEXT_COLOR);
			
			String labelText;
			if (recipients != null) {
				// It's an outgoing private message
				labelText = "<html><b>To " + recipients + ":</b> " + messageText + "</html>";
			} else {
				// It's an incoming private message
				labelText = "<html><b>From " + sender + ":</b> " + messageText + "</html>";
			}
			
			JLabel messageLabel = new JLabel(labelText);
			messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			messageLabel.setForeground(PRIVATE_TEXT_COLOR);
			
			privateMessagePanel.add(iconLabel, BorderLayout.WEST);
			privateMessagePanel.add(messageLabel, BorderLayout.CENTER);
			
			bubblePanel = privateMessagePanel;
			
			// Add individual private message box directly to private messages panel
			addPrivateMessageBox(bubblePanel);
		} else {
			// For broadcast messages, use the bubble renderer
			bubblePanel = ChatBubbleRenderer.createBubble(messageText, sender, isOwnMessage);
			
			// Add broadcast message to the broadcast panel
			addPanelToMessageArea(bubblePanel, broadcastPanel);
		}
	}
	
	/**
	 * Add an individual private message box to the private messages panel
	 * @param messageBox The private message box to add
	 */
	private void addPrivateMessageBox(JPanel messageBox) {
		// Add separator if not the first message
		if (privateMessagesPanel.getComponentCount() > 0) {
			JSeparator separator = new JSeparator();
			separator.setForeground(PRIVATE_BORDER_COLOR);
			separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
			separator.setAlignmentX(LEFT_ALIGNMENT);
			privateMessagesPanel.add(separator);
			privateMessagesPanel.add(Box.createVerticalStrut(5));
		}
		
		// Add message box
		privateMessagesPanel.add(messageBox);
		privateMessagesPanel.add(Box.createVerticalStrut(5));
		privateMessagesPanel.revalidate();
		privateMessagesPanel.repaint();
		
		// Scroll to bottom
		SwingUtilities.invokeLater(() -> {
			JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(
				JScrollPane.class, privateMessagesPanel);
			if (scrollPane != null) {
				JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
				verticalBar.setValue(verticalBar.getMaximum());
			}
		});
	}
	
	/**
	 * Helper method to add a panel to message area with proper spacing
	 * @param panel Panel to add
	 * @param targetArea Target message area
	 */
	private void addPanelToMessageArea(JPanel panel, JPanel targetArea) {
		// Add separator line if not the first message
		if (targetArea.getComponentCount() > 1) {
			JSeparator separator = new JSeparator();
			separator.setForeground(BROADCAST_SEPARATOR_COLOR);
			separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
			targetArea.add(separator, targetArea.getComponentCount() - 1);
		}
		
		// Add message panel
		targetArea.add(panel, targetArea.getComponentCount() - 1);
		targetArea.revalidate();
		targetArea.repaint();
		
		// Scroll to bottom
		SwingUtilities.invokeLater(() -> {
			JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(
				JScrollPane.class, targetArea);
			if (scrollPane != null) {
				JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
				verticalBar.setValue(verticalBar.getMaximum());
			}
		});
	}
	
	/**
	 * Send a message, to be relayed to all chatters
	 * @param chatMessage
	 * @throws RemoteException
	 */
	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.updateChat(name, chatMessage);
	}

	/**
	 * Send a message, to be relayed, only to selected chatters
	 * @param chatMessage
	 * @throws RemoteException
	 */
	private void sendPrivate(int[] privateList) throws RemoteException {
		String privateMessage = message;
		privateMessage = name + ": " + privateMessage;
		chatClient.serverIF.sendPM(privateList, privateMessage);
		
	}
	
	/**
	 * Method to process incoming messages from the server
	 * @param message The message text
	 */
	public void processIncomingMessage(String message) {
		// Check if it's a private message
		if (message.startsWith("[PM from")) {
			int endOfSender = message.indexOf("]");
			if (endOfSender > 0) {
				String sender = message.substring(9, endOfSender);
				String messageText = message.substring(endOfSender + 3).trim();
				addChatMessage(sender, messageText, true, null);
			}
		}
		// Check if it's a server message
		else if (message.startsWith("[Server]")) {
			addSystemMessage(message);
		}
		// Regular broadcast message
		else {
			int colonIndex = message.indexOf(":");
			if (colonIndex > 0) {
				String sender = message.substring(0, colonIndex).trim();
				String messageText = message.substring(colonIndex + 1).trim();
				addChatMessage(sender, messageText, false, null);
			} else {
				// If the format is not recognized, just add it as a system message
				addSystemMessage(message);
			}
		}
	}

	/**
	 * Make the connection to the chat server
	 * @param userName
	 * @throws RemoteException
	 */
	private void getConnected(String userName) throws RemoteException{
		//remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+","_");
		cleanedUserName = userName.replaceAll("\\W+","_");
		try {		
			chatClient = new ChatClient3(this, cleanedUserName);
			chatClient.startClient();
			
			// Set textArea reference to broadcastPanel for compatibility
			textArea = broadcastPanel;
			
			if(!chatClient.connectionProblem) {
				// Make sure to enable the send button
				SwingUtilities.invokeLater(() -> {
					startButton.setEnabled(false);
					sendButton.setEnabled(true);
				});
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}//end class

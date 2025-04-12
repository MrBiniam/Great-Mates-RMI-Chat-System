# Great Mates RMI Chat: Technical Study Guide
*Dilla University - Introduction to Distributed Systems Course Project*

## 1. Understanding RMI and Distributed Systems

### What is RMI?
Java Remote Method Invocation (RMI) is a mechanism that allows an object running in one Java Virtual Machine (JVM) to invoke methods on an object running in another JVM. RMI provides a framework to build distributed applications in Java.

### RMI and Distributed Systems Connection
RMI directly relates to distributed systems by:
- **Location Transparency**: Clients can call methods on remote objects as if they were local
- **Object-Based Communication**: Unlike basic socket programming, RMI passes complete objects
- **Distributed Garbage Collection**: Automatic resource management across network
- **Fault Tolerance**: Handles network errors and provides appropriate exceptions

### Key Distributed Systems Concepts Demonstrated
1. **Client-Server Architecture**: Clear separation of responsibilities
2. **Middleware Communication**: RMI serves as middleware between distributed components
3. **State Management**: Maintaining user sessions and message delivery
4. **Concurrency**: Handling multiple clients simultaneously

## 2. Project Architecture Overview

### Hub and Spoke Topology
Our chat application follows a centralized topology where:
- Server acts as the central hub
- Each client (spoke) connects independently to the server
- All communication flows through the server, even private messages

### Component Breakdown
1. **RMI Registry**: Central naming service that clients use to find the server
2. **Server**: Manages connections, routes messages, tracks users
3. **Clients**: User interface and local representation of the remote service

## 3. Key Project Components

### Server-Side (`server` package)
- **ChatServer.java**: Core server implementation that:
  - Registers with RMI Registry
  - Maintains list of connected users
  - Routes messages between clients
  - Handles client disconnections
  
- **ChatServerIF.java**: Remote interface defining methods clients can call:
  - `registerChatter()`: Register a new chat client
  - `updateChat()`: Broadcast messages
  - `sendPM()`: Send private messages
  - `leaveChat()`: Handle client disconnection

### Client-Side (`client` package)
- **SplashScreen.java**: Welcome screen with Great Mates branding
  
- **ChatClient3.java**: Client-side RMI implementation that:
  - Connects to the server
  - Handles remote callbacks from server
  - Manages the client's RMI service
  
- **ClientRMIGUI.java**: Main user interface that:
  - Provides chat window with dual panel design
  - Manages message display and routing
  - Handles user interactions
  - Implements custom styling for message types
  
- **ChatBubbleRenderer.java**: Custom rendering for message bubbles

## 4. Message Flow Explained

### Broadcast Message Path
1. User types message in the input field
2. Client calls `updateChat()` on remote server object
3. Server iterates through connected clients
4. Server calls `messageFromServer()` on each client
5. Client's `processIncomingMessage()` displays message in broadcast panel

### Private Message Path
1. User selects recipients and checks "PM" box
2. Client calls `sendPM()` on remote server
3. Server formats message with "[PM from Username]" prefix
4. Server calls `messageFromServer()` only on selected clients
5. Receiving clients display message in golden private message panel

## 5. UI/UX Design Implementation

### Dual Panel Layout
- **Top Panel**: Light turquoise background for broadcast messages
- **Bottom Panel**: Individual golden boxes for private messages

### Message Styling
- **Broadcast Messages**: Chat bubbles with sender name
- **Private Messages**: Golden background with black text and sender identification
- **System Messages**: Centered with italics

### Color Scheme
- Background: #F4F6F9 (soft gray)
- Broadcast Panel: #E8F8F5 (light turquoise)
- Private Messages: #FDE8A8 (golden)
- Text: #2C3E50 (dark blue-gray)

## 6. Running and Testing the Application

### Required Environment
- Java Development Kit (JDK) 8 or higher
- Windows, Linux, or macOS

### Execution Steps
1. **Compile the Code**:
   ```
   javac -d bin src/client/*.java src/server/*.java
   ```

2. **Start the RMI Registry**:
   ```
   cd bin
   rmiregistry
   ```

3. **Launch the Server**:
   ```
   cd ..
   java -cp bin server.ChatServer
   ```

4. **Start Client Applications**:
   ```
   java -cp bin client.SplashScreen
   ```

### Testing Functionality
1. **User Registration**: Enter unique usernames on each client
2. **Broadcast Messaging**: Type messages and press Send
3. **Private Messaging**: Select users, check PM box, type message, press Send
4. **User Disconnect**: Close client window and observe server logs

## 7. Common Issues and Solutions

### Registry Connection Problems
- Ensure RMI registry is running in the bin directory
- Check for firewall blocking Java processes
- Verify hostname resolution

### Message Display Issues
- If messages appear in the wrong panel, verify formatting
- For private messages, ensure they have the "[PM from...]" prefix

### Client Disconnections
- Server will automatically detect and notify other users
- Disconnected users will be removed from user lists

---

*This study guide was prepared by the Great Mates team for the Introduction to Distributed Systems course at Dilla University, 2025.*

# RMI Chat Application

## Developed by Great Mates
*Dilla University - 4th Year Students - Introduction to Distributed Systems Course Project*

![Dilla University](https://dillauniversity.edu.et/wp-content/uploads/2023/01/Dilla_University-1.png)

## Overview
A modern, elegant chat application built with Java RMI (Remote Method Invocation) technology. This distributed system demonstrates practical implementation of remote object communication in a real-world application.

## Features
- **Beautiful Modern UI**: Intuitive interface with clean design and elegant styling
- **Dual Message Display**: 
  - Light turquoise panel for broadcast messages
  - Individual golden boxes for private messages
- **Real-time Communication**: Instant message delivery across distributed clients
- **Private Messaging**: Send secure messages to selected users
- **User Status Updates**: Real-time notifications when users join or leave
- **Splash Screen**: Attractive welcome screen with group branding

## Quick Start Guide

### Running the Application
1. **Start the RMI Registry**:
   ```
   cd bin
   rmiregistry
   ```

2. **Launch the Server**:
   ```
   java -cp bin server.ChatServer
   ```

3. **Start Client Applications**:
   ```
   java -cp bin client.SplashScreen
   ```

4. **Begin Chatting**:
   - Enter your username
   - Send broadcast messages to everyone
   - Select users and check "PM" for private messages

## Project Structure
- **Client Package**: UI components and client-side RMI implementation
- **Server Package**: Chat server and client connection management
- **RMI Framework**: Handles communication between distributed objects

## Enhancement Details
This project builds upon original work by Daragh Walshe (2015) with significant UI/UX improvements, including:
- Redesigned message panels with enhanced visual distinction
- Individual styled message boxes for private communications
- Improved user experience and interaction flow
- Custom splash screen with group branding

---

*© 2025 Great Mates Team - Dilla University*

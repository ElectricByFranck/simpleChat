package edu.seg2105.edu.server.backend;
import edu.seg2105.server.common.*;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;



/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
	
	ChatIF serverUI;
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 55;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  @Override
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
      String message = msg.toString();

      // Check if client is not logged in
      if (client.getInfo("loginId") == null) {
          if (message.startsWith("#login ")) {
              String loginId = message.substring(7).trim();

              if (loginId.isEmpty()) {
                  try {
                      client.sendToClient("Login ID cannot be empty. Connection terminated.");
                      client.close();
                  } catch (IOException e) {
                      System.out.println("Issues occur during closing the connection: " + e.getMessage());
                  }
                  return;
              }

              // Save login ID directly in the client's info
              client.setInfo("loginId", loginId);
              System.out.println("Client logged in with the ID: " + loginId);

              try {
                  client.sendToClient("Welcome " + loginId + "!");
              } catch (IOException e) {
                  System.out.println("Error while sending the welcome message: " + e.getMessage());
              }
          } else {
              try {
                  client.sendToClient("You must login first using #login <id>. Connection is now terminated.");
                  client.close();
              } catch (IOException e) {
                  System.out.println("Problem while closing the connection: " + e.getMessage());
              }
          }
          return;
      }

      // If already logged in
      String loginId = (String) client.getInfo("loginId");
      System.out.println("Message received: " + message + " from " + loginId);
      this.sendToAllClients(loginId + "> " + message);
  }


  
  @Override
  
  //Changing the behaviour of this method so when the client is disconnected, he get a personalized message
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	    String loginId = (String) client.getInfo("loginId");
	    System.out.println("Client disconnected: " + loginId);
	}
  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server is listening for clients on port : " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("The Client is connected on port number : " + getPort());
		try {client.sendToClient("Welcome to my chat server !");}
		catch(IOException e) {System.out.println("Failed to send welcome message: " + e.getMessage());}
	}
	
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
	  int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
     	port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections

    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients! " + ex.getMessage());
    }
  }
}
//End of EchoServer class

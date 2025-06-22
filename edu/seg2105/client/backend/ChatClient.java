// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String host = "";
  int port;
  private String login_id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginId) throws IOException{
    super(host, port); //Call the superclass constructor
    this.login_id = loginId;
    this.clientUI = clientUI;
  
  if(loginId==null||loginId.trim().isEmpty()) {
	  System.out.println("Login ID is required. Terminating.");
	  System.exit(1);
  }
  
  openConnection();//Opening the connection
  }
 
  
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
	    clientUI.display(msg.toString()); // Displaying message from the server 
	}

  
  @Override
  //Aknowledging the connection and if successfull sending the idd to the server
  protected void connectionEstablished() {
      System.out.println("The connection with the server has been made !");
      
      try {
          sendToServer("#login <" + login_id + ">");
      } catch (IOException e) {
          clientUI.display("Failed to send login ID to server.");
          quit();
      }
  }



  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message){
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  //If the connection with the server is cleanly terminated
  public void connectionClosed() {
	  System.out.println("Connection closed.");
  }
  
  	//IF the connection with the server is unexpectedly interrupted
	protected void connectionException(Exception exception) {
		System.out.println("The connection with the server has been unexpectedly interrupt !");
		quit();
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class

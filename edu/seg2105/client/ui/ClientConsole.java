package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  //Assigning the default port number as the user hasn't provide the port number
  public static int port;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  private String login_id;
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginId) 
  {
	  this.login_id = loginId;
	  
    try 
    {
      client= new ChatClient(host, port,this, login_id);
      
    } 

    catch(IOException exception) 
    {
      System.out.println("ERROR - Can't setup connection! Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try {	
    	String message;

    	while (true){
        message = fromConsole.nextLine();
        
        	if(!message.startsWith("#")) {
        		client.handleMessageFromClientUI(message);
        		//System.out.println("CLIENT MSG> " + message);
        	}
        
        	//Causes the client to terminate gracefully.
        	else if(message.equals("#quit")) {
        		System.out.println("Client is disconnecting from the server and terminating cleanly....");
      	  		client.quit();
      	  		
        	}
        
        	//Causes the client to disconnect from the server, but not quit.
        	else if(message.equals("#logoff")) {
      	  		try{client.closeConnection();}
      	  		catch(IOException e) {System.out.println("Error occur while trying to close the connection with the server :" + e.getMessage());}
        	}
        	
      	  	// Calls the setHost method in the client. Only allowed if the client is logged 
      	  	//off; displays an error message otherwise.
      	  	else if(message.startsWith("#sethost")) {//#sethost <host>
      	  		if(client.isConnected()) {
      	  			System.out.println("You need to disconnect first !!");
      	  		}else {
          	  		int start = message.indexOf("<");
          	  		int end = message.indexOf(">");
          	  		try {String newHost = message.substring(start+1,end); client.setHost(newHost);System.out.println("Client Host set to: "+ newHost);}
          	  		catch(Exception e) {System.out.println("An error has occur when extracting the hostname, check your entry");}  	
      	  		}
      	  	}
      	  	
      	  //Calls the setPort method in the client, with the same constraints as sethost.
      	  else if(message.startsWith("#setport")) {//#setport <port>
    	  		if(client.isConnected()) {
      	  			System.out.println("Disconnecting from current server...");
      	  			
      	  			try {
      	  				client.closeConnection();}
      	  				catch(IOException ex){System.out.println("Error whille disconnecting: ");}
      	  			}
    	  		
      	  			int start = message.indexOf("<");
      	  			int end = message.indexOf(">");
      	  			try {
      	  				int newPort = Integer.parseInt(message.substring(start+1,end)); 
      	  				client.setPort(newPort);
      	  				System.out.println("Client port set to: "+ newPort);

      	  				try {client.openConnection();}
      	  				catch(IOException ex) { System.out.println("Error while reconnecting: ");}
      	  			}
      	  			catch(Exception e) {System.out.println("An error has occur when extracting the port number, check your entry");}  

      	  }
           	  	
      	  //uses the client to connect to the server. Only allowed if the client is not already connected
      	  else if(message.equals("#login")) {
      		  if(!client.isConnected()) {
      			  try {client.openConnection();} 
      			  catch(IOException e) {System.out.println(e.getMessage() + " An error occured while connecting to the server");} 
      		  }
      		  else {System.out.println("The client is already connected");}
      	  }
      	  	
      	  // Displays the current port number
      	  else if(message.equals("#getport")) {
      		  port = client.getPort();
      		  System.out.println("The port number is :"+port);
      	  }
      	  	
      	  // Displays the current host name.
      	  else if(message.equals("#gethost")) {
      		  String host = client.getHost();
      		  System.out.println("The new host name is : " + host);
      	  }

    	}
    }
    catch (Exception ex){System.out.println("Unexpected error while reading from console!");}
  }


  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
	  System.out.println(message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */

  public static void main(String[] args) 
  {
	  String loginID = null;
	  String host;
	  int port;
	  
	//Validate login ID
	  if (args.length < 1 || args[0].trim().isEmpty()) {
	      System.out.println("ERROR - No login ID specified. Connection aborted.");
	      System.exit(1);
	  } else {
		  
		  //Assigning the loginId with the argument provided
	      loginID = args[0].trim();
	  }

	//Assigning hostname
	  if (args.length >= 2 && !args[1].trim().isEmpty()) {
	      host = args[1].trim();
	  } else {
	      host = "localhost"; // default
	  }
	  
	  
	//Assign port number
	  if (args.length >= 3) {
	      try {
	          port = Integer.parseInt(args[2].trim());
	      } catch (NumberFormatException e) {
	          System.out.println("Warning: Invalid port format. Using default port " + DEFAULT_PORT + ".");
	          port = DEFAULT_PORT;
	      }
	  } else {
	      port = DEFAULT_PORT; // default port number
	  }

	  //Starting a new connection
  ClientConsole chat = new ClientConsole(host, port, loginID);
  chat.accept(); //Wait for console data

  }
}
//End of ConsoleChat class

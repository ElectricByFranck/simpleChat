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
  final public static int DEFAULT_PORT = 55;
  
  
  //Assigning the default port number as the user hasn't provide the port number
  public static int port = DEFAULT_PORT;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
      
    } 

    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!" + " Terminating client." + exception.getMessage());
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
        		//client.handleMessageFromClientUI(message);
        		display(message);
        		//System.out.println("CLIENT MSG> "+message);
        	}
        
        	//Causes the client to terminate gracefully.
        	else if(message.equals("#quit")) {
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
      	  		int start = message.indexOf("<");
      	  		int end = message.indexOf(">");
      	  		try {String newHost = message.substring(start+1,end); client.setHost(newHost);}
      	  		catch(Exception e) {System.out.println("An error has occur when extracting the substring, verify your message");}  
      	  	}
      	  	
      	  //Calls the setPort method in the client, with the same constraints as sethost.
      	  else if(message.startsWith("#setport")) {//#setport <port>
      		  int start = message.indexOf("<");
      		  int end = message.indexOf(">");
      		  try {int newPort = Integer.parseInt(message.substring(start+1,end)); client.setPort(newPort);}
      		  catch(Exception e) {System.out.println("An error has occur when extracting the substring, verify your message");}  
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
    System.out.println("SERVER MSG> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */

  public static void main(String[] args) 
  {
    String host = "";

    	
    	//Handling the error case where the user doesn't provide the host name
    	if(args.length>0) { 
    		try {host = args[0];}
    		catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println(e.getMessage() + "Host hasn't been provided, Now using the default host name");
    			
    			//Assigning the default host name as the user hasn't provide the host name
    			host = "localhost";}
    		}
    		
    	//Handling the error case where the user doesn't provide the port number
    	if(args.length>1) {
      		try { port = Integer.parseInt(args[1].trim()); }
      		catch(Exception e){ 
      			System.out.println(e.getMessage() + "The format used isn't accepted, Now using the default port number"); 
      			
      			}
      	}

    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data

  }
}
//End of ConsoleChat class

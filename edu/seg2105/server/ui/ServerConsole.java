package edu.seg2105.server.ui;
import edu.seg2105.edu.server.backend.*;
import edu.seg2105.server.common.ChatIF;
//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 
import java.io.*;
import java.util.Scanner;


public class ServerConsole implements ChatIF {
	
	 //The instance of the user that created this ConsoleChat.
	  EchoServer server;
	  
	  //Scanner to read from the console
	  Scanner fromConsole; 
	  
	  private static int port;
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	

	  //This method overrides the method in the ChatIF interface.  It displays a message onto the screen.
	  public void display(String message) {
		  System.out.println("SERVER MSG> " + message);
	  }
	  
	  
	  public ServerConsole(int port) {
		  this.port = port;
		  
		  // Start listening for client connections
		  try { server = new EchoServer(port); server.listen();
		  //System.out.println("Server started >>>");
		  }
		  
		  catch(Exception e) {System.out.println("Something went wrong while listement to port :"+ port + e.getMessage());System.exit(1); }
		  
		    // Create scanner object to read from console
		    fromConsole = new Scanner(System.in);
	  }
	  
	  public void quit() {
		    try {
		        server.sendToAllClients("Server is shutting down...");
		        server.close(); // Gracefully close all connections and the server
		        System.out.println("Server has shut down successfully.");
		    } catch (IOException e) {
		        System.out.println("Error shutting down server: " + e.getMessage());
		    }
		    System.exit(0);
		}
	  
	  
	  public void accept(){
	    try{
	    	String message;

	    	while (true){
	        message = fromConsole.nextLine();
	        
	        if(!message.startsWith("#")){
		        server.sendToAllClients("SERVER MSG> "+ message);
		        display(message);
	        }
	        
	  	  	//Causes the client to terminate gracefully.
	  	  	else if(message.equals("#quit")) {
	  		  quit();
	  	  	}
	      
	        //Causes the server to stop listening for new clients.
	  	  	else if(message.equals("#stop")) {
	  		  server.stopListening();
	  	  	}
	        
	        //Causes the server not only to stop listening for new clients, but also to disconnect all existing clients.
	  	  	else if(message.equals("#close")) {
	  		  server.close();
	  	  	}
	      
	        //#setport <port> Calls the setPort method in the server. Only allowed if the server is closed.
	  	  	else if(message.startsWith("#setport")) {//#setport <port>
 	  			int start = message.indexOf("<");
  	  			int end = message.indexOf(">");
  	  			
  	  			try {
  	  				
  	  			int newPort = Integer.parseInt(message.substring(start+1,end));
  	  			
  	    	  		if(server.isListening()) {
  	    	  			
  	      	  			System.out.println("You're switching to a new port!!");
  	  	    	  		server.close(); //Fully closing the server socket
  	  	  				server.setPort(newPort);//Changing the port number
  	  	  				this.port = newPort;
  	  	  				server.listen(); //Now listening on the new port
  	  	  				System.out.println("Server Port switched to: "+ newPort);
  	  	  				System.out.println("Waiting for the completing of adding the new port");
  	  	  				try {
  	  	  					Thread.sleep(2000);  // Delay execution for 2 seconds
  	  	  				} catch (InterruptedException e) {System.out.println("Sleep was interrupted");e.printStackTrace();
  	  				}
  	  				
  	  				System.out.println("Server listening for clients on port : " + server.getPort());

  	      	  			
  	      	  		}else {
  	    	  		
  	  				server.setPort(newPort);//Changing the port number
  	  				this.port = newPort;
  	  				System.out.println("Server Port switched to: "+ newPort);}
  	  			}
  	  			
      	  			catch(Exception e) {System.out.println("An error has occur when extracting the port number, check your entry"+e.getMessage());}  
	  	  	}
	        
	        //Causes the server to start listening for new clients. Only valid if the server is stopped.
	  	  	else if(message.equals("#start")) {
	  	  		if(!server.isListening()) {
	  	  			try{server.listen();}
	  	  			catch(IOException e) {System.out.println("An error has occur when retart listening "+ e.getMessage());}
	  	  		}
	  	  		
	  	  	//System.out.println("The server is already running");
	        }
	        
	        //Displays the current port number. 
	  	  	else if(message.equals("#getport")) {
	  		  System.out.println("The port number is : "+ server.getPort());
	  	  	}

	        }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("Unexpected error while reading from console!");
	    }
	  }
	  
	  public static void main(String[] args) {
		  int port;


		    try
		    {
		      port = Integer.parseInt(args[0]); //Get port from command line
		    }
		    catch(Throwable t)
		    {
		     	port = DEFAULT_PORT; //Set port to 5555
		    }
			  
			  ServerConsole chat = new ServerConsole(port);
			  chat.accept();
		  
		  
	  }

}

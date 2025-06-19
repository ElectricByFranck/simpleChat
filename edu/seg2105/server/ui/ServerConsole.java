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
	  
	  public static int port;
	

	  //This method overrides the method in the ChatIF interface.  It displays a message onto the screen.
	  public void display(String message) {
		  System.out.println("SERVER MSG> " + message);
	  }
	  
	  
	  public ServerConsole(int port) {
		  this.port = port;
		  try { server = new EchoServer(port);}
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
		        server.sendToAllClients("SERVER MSG>" + message);
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
	  	  		try {int newPort = Integer.parseInt(message.substring(start+1,end)); server.setPort(newPort);}
	  	  		catch(Exception e) {System.out.println("An error has occur when extracting the substring, verify your message");}  
	  	  	}
	        
	        //Causes the server to start listening for new clients. Only valid if the server is stopped.
	  	  	else if(message.equals("#start")) {
	  	  		if(!server.isListening()) {
	  	  			try{server.listen();}
	  	  			catch(IOException e) {System.out.println("An error has occur when retart listening "+ e.getMessage());}
	  	  		}
	        }
	        
	        //Displays the current port number. 
	  	  	else if(message.equals("#getport")) {
	  		  System.out.println("The port number is :"+ server.getPort());
	  	  	}

	        }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	  
	  public static void main(String[] args) {
		  ServerConsole chat = new ServerConsole(port);
		  chat.accept();
		  
	  }

}

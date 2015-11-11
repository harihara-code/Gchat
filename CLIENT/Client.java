package Gchat.CLIENT;

import java.net.*;
import java.io.*;

public class GchatClient implements Runnable{

    private Socket serverSocket = null;
    private Gchat.NetworkSocket socket = null;
    private volatile boolean consoleLock = false;

    public static void main(String args[]) throws Exception {

     //Instantiates Gchat.Client class object
       GchatClient clientObj = new GchatClient();

     //Instantiates Gchat.NetworkSocket class object
	   clientObj.socket = new Gchat.NetworkSocket();
      
     //Creating Network socket to connect to the server
       clientObj.serverSocket = clientObj.socket.createSocket("127.0.0.1",7872);
       System.out.println("Socket Created");

     //Receive the welcome message from the server
       System.out.println(clientObj.socket.receiveMessage(clientObj.serverSocket));
    
     //Run new Thread to perform message receiving operation that sent by the server
       Thread newThread = new Thread(clientObj);
       newThread.start();
       
       String clientOption = null;
       BufferedReader consoleScreen = new BufferedReader(new InputStreamReader(System.in));

       do {
		    //Display Menu Option for client to perform chat and exit GchatClient Application
		      System.out.println("Chat - Enter c");
		      System.out.println("Exit - Enter e");
		      clientOption = consoleScreen.readLine();
		     
		    //set consoleLock = true to tell that client is busy in chatting
		      clientObj.consoleLock = true;


		    //If Client choose chat option
		      if(clientOption.equals("c")) {

		      	System.out.print("Enter the Message : ");
		      	String message = consoleScreen.readLine();

		      //Sending message to the server
		      	clientObj.socket.sendMessage(clientObj.serverSocket,message);
		      	
		      //Chat Operation is completed clientConsoleScreen is free now 
		      	clientObj.consoleLock = false;

		      } else if(clientOption.equals("e")) {
		     		 
		     		 //If Client want to exit from GchatClient Application
		      		
		      		 //Sending message to the server
		    		   clientObj.socket.sendMessage(clientObj.serverSocket,"quit");
					   System.out.println("Outcoming Closed");
		      		 
		      		 //Chat Operation is completed clientConsoleScreen is free now 
		      		   clientObj.consoleLock = false;
		      
			         //Wait until newThread terminate its execution
					   newThread.join();
		      		   System.out.println("Incoming Closed");

		        } else {
	      			
      	  			 //If Client choose invalid menu option
					   clientObj.consoleLock = false;
	      		   	   System.out.println("Invalid Menu Option please try again.");
	      
      			  }

    } while(!clientOption.equals("e"));
}

public void run() {
     
    String message = null;

	do {
	
		//Get the message from the server.
		  message = socket.receiveMessage(serverSocket);

		//If client Console Screen is busy in chatting. 
		  if(consoleLock) {
				
				//Wait until Client Console Screen is free
				  while(consoleLock) {

			      }
	      }

		//Client Console screen is free now.
	  	  System.out.println("Server :"+message);	 
        
    } while(!message.equals("quit"));

}

///}
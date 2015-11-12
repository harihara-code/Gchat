///////////////////////////////////////////////////////////////////////////
/////File Name : GchatClient.java////////////////////////////////////////
/////Coded by  : hariharan sathyanarayanan/////////////////////////////////
/////copyright @ 2015 <harihara95@gmail.com>///////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.CLIENT;

import java.net.*;
import java.io.*;

//GchatClient - Client program 
public class GchatClient implements Runnable
{

    private Socket serverSocket = null; 
    private Gchat.NetworkSocket socket = null;
    private volatile boolean consoleLock = false;
 
    public static void main(String args[]) throws Exception 
    {
     //Instantiates GchatClient class 
      GchatClient clientObj = new GchatClient();

	//Instantiates Gchat.NetworkSocket class 
	  clientObj.socket = new Gchat.NetworkSocket();

      BufferedReader consoleScreen = new BufferedReader(new InputStreamReader(System.in));
   //We connect to the server through creating socket to it
      clientObj.serverSocket = clientObj.socket.createSocket("127.0.0.1",7298);
   
      //System.out.println("Socket Created");
      //testing
       	//clientObj.socket.sendMessage(clientObj.serverSocket,"quit");

   //Notify client is connected to the server
      System.out.println("GchatServer is connected");

   //Client name is sent to the server to identify itself
      System.out.print("Enter the client name :");
      String name = consoleScreen.readLine();
      clientObj.socket.sendMessage(clientObj.serverSocket,name);

   //Receive the welcome message from the server
      System.out.println(clientObj.socket.receiveMessage(clientObj.serverSocket));
    

     //TESTING PHASE
     //  clientObj.socket.sendMessage(clientObj.serverSocket,"OK I'm Received YOUR MESSAGE!!\n")

     //Create new Thread to handle receiving message operation
      Thread newThread = new Thread(clientObj);
      newThread.start();
       
      String clientOption = null;
    do {
    //Display Client Menu Options
      System.out.println("Menu Options\n-----------------");
      System.out.println("1.To Chat - Enter c");
      System.out.println("2.To see Who's online - Enter v");
      System.out.println("3.To Exit - Enter e");
      clientOption = consoleScreen.readLine();
     
    //Enable the console lock to tell client ConsoleScreen is busy in chatting.
      clientObj.consoleLock = true;

      if(clientOption.equals("c")) {

      	System.out.print("Enter the Message to send : ");
      	String message = consoleScreen.readLine();
	 
	 //Sending message to the server
      	clientObj.socket.sendMessage(clientObj.serverSocket,message);
      	
      //Chat Operation is completed clientConsoleScreen is free now 
      	clientObj.consoleLock = false;
      
      }
      else if(clientOption.equals("v")) 
      {

      //Sending whosonline signal to the server
      	clientObj.socket.sendMessage(clientObj.serverSocket,"whos");
      //Free the consoleLock
      	clientObj.consoleLock = false;
		
      }
      
      else if(clientOption.equals("e")) 
      {
      //Sending message to the server
      	clientObj.socket.sendMessage(clientObj.serverSocket,"quit");
		//System.out.println("Outcoming Closed");
      //Chat Operation is completed clientConsoleScreen is free now 
      	clientObj.consoleLock = false;
      
	  //Wait until newThread terminate its execution
		newThread.join();
      	//System.out.println("Incoming Closed");
	  
	  }
      
      else 
      {
      //Chat Operation is completed clientConsoleScreen is free now 
      	clientObj.consoleLock = false;
      	System.out.println("Invalid Menu Option please try again.");
      
      }

    } while(!clientOption.equals("e"));
}

public void run() 
{
  
    String message = null;

	do {

	//Get the message from the server.
	  message = socket.receiveMessage(serverSocket);

	//If client Console Screen is busy. 
	  if(consoleLock) {
      //console screen is busy in chatting 
   	  	while(consoleLock) {

	  	}
	  }

	//Check disconnect signal
	  if(!message.equals("disconnect"))
		//console screen is free now
	  	System.out.println(message);

	 } while(!message.equals("disconnect"));
	 //System.out.println("disconnect signal sent by the server");

	}
}
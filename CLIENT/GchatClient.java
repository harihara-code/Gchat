///////////////////////////////////////////////////////////////////////////
//// File Name : GchatClient.java            //////////////////////////////
//// Coded by  : hariharan sathyanarayanan   //////////////////////////////
//// Copyright @ 2015 <harihara95@gmail.com> //////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.CLIENT;

import java.net.*;
import java.io.*;

//GchatClient-Client program used to connect to the GchatServer program
public class GchatClient implements Runnable
{
    private Socket GchatServerSocket = null; 
    private Gchat.NetworkSocket socket = null;
    private volatile boolean consoleLock = false;

    public static void main(String args[]) throws Exception 
    {
      BufferedReader consoleScreen = new BufferedReader(new InputStreamReader(System.in));
      GchatClient clientObj = new GchatClient();
      
      clientObj.socket = new Gchat.NetworkSocket();
      
    //We connect to the GchatServer through creating socket to it
      clientObj.GchatServerSocket = clientObj.socket.createSocket("127.0.0.1",7298);
    
    //Notify that client is connected to the Gchatserver
      System.out.println("GchatServer is connected");
      
      System.out.print("Enter the client name :");
      String name = consoleScreen.readLine();
    
    //Client name is sent to the GchatServer to identify itself
      clientObj.socket.sendMessage(clientObj.GchatServerSocket,name);
    
    //Receive the response from the GchatServer
      String serverResponse = clientObj.socket.receiveMessage(clientObj.GchatServerSocket);
      System.out.println("GchatServer : "+serverResponse);
    
    //If GchatServer cannot accept our connect request
      if(serverResponse.equals("Maximum client Limit Reached"))
      {
      //Close the GchatServerSocket 
        clientObj.GchatServerSocket.close();
    
      //GchatClient program will terminate
        System.out.println("GchatClient Terminated");
        System.exit(-1);
      }
    
    //Create new Thread to handle receiving message operation
      Thread newThread = new Thread(clientObj);
      newThread.start();
       
      String clientOption = null;
      do
       {
      //Display Client Menu Options
        System.out.println("Menu Options\n-----------------");
        System.out.println("1.To Chat - Enter c");
        System.out.println("2.To see Who's online - Enter v");
        System.out.println("4.To Exit - Enter e");
        System.out.print("Enter the valid menu option : ");
        clientOption = consoleScreen.readLine();
    
      //Enable the console lock to tell client ConsoleScreen is busy in chatting.
        clientObj.consoleLock = true;

        if(clientOption.equals("c"))
          {
           	System.out.print("Enter the Message to send : ");
           	String message = consoleScreen.readLine();
	   
          //Sending message to the server
      	    clientObj.socket.sendMessage(clientObj.GchatServerSocket,message);
     
      	  //Chat Operation is completed clientConsoleScreen is free now 
      	    clientObj.consoleLock = false;
         }
         else if(clientOption.equals("v")) 
         {
          //Sending whosonline signal to the server
      	    clientObj.socket.sendMessage(clientObj.GchatServerSocket,"whos");
     
          //Free the consoleLock
      	    clientObj.consoleLock = false;
         }
         else if(clientOption.equals("e")) 
         {
          //Sending message to the server
      	    clientObj.socket.sendMessage(clientObj.GchatServerSocket,"quit");
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

      }while(!clientOption.equals("e"));
      System.out.println("GchatClient Terminated");
    }

<<<<<<< HEAD
  //Receive message from GchatServer
    public void run() 
    {
      String message = null;
      do
      {
      //Get the message from the server.
    	  message = socket.receiveMessage(GchatServerSocket);
      //If client console screen is busy in chatting 
    	  if(consoleLock) 
        {
          //console screen is busy in chatting 
       	  	while(consoleLock) 
            {
      	    }
    	  }

    	//Check disconnect signal sent
    	  if(!message.equals("disconnect"))
    		//console screen is free now
    	  	System.out.println("\n"+message);

    	 } while(!message.equals("disconnect"));
    }
}

public void run() 
{
  String message = null;
  do
  {
  //Get the message from the server.
    message = socket.receiveMessage(GchatServerSocket);
  //If client Console Screen is busy. 
    if(consoleLock) 
    {
     //console screen is busy in chatting 
       while(consoleLock) 
        {
  	} 
    }
  //Check disconnect signal sent
    if(!message.equals("disconnect"))
     //console screen is free now
       System.out.println("\n"+message);
  } while(!message.equals("disconnect"));
 //System.out.println("disconnect signal sent by the server");
}
}


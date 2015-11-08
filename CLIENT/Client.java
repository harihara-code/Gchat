package Gchat.CLIENT;

import java.net.*;
import java.io.*;

public class Client implements Runnable{

    private Socket serverSocket = null;
    private Gchat.NetworkSocket socket = null;
    private volatile boolean consoleLock = false;

    public static void main(String args[]) throws Exception {
    //Instantiates Gchat.Client class object
      Client clientObj = new Client();

	//Instantiates Gchat.NetworkSocket class object
	  clientObj.socket = new Gchat.NetworkSocket();
      
    //Creating Network socket to connect to the server
      clientObj.serverSocket = clientObj.socket.createSocket("127.0.0.1",7872);
      System.out.println("Socket Created");

    //Receive the welcome message from the server
      System.out.println(clientObj.socket.receiveMessage(clientObj.serverSocket));

    //Create new Thread to handle receiving message operation
      Thread newThread = new Thread(clientObj);
      newThread.start();
       
      String clientOption = null;
      BufferedReader consoleScreen = new BufferedReader(new InputStreamReader(System.in));

    do {
    //Display Client Menu Interface
      System.out.println("Chat - Enter c");
      System.out.println("Exit - Enter e");
      clientOption = consoleScreen.readLine();
     
    //Enable the console lock to tell client ConsoleScreen is busy in chatting.
      clientObj.consoleLock = true;
      if(clientOption.equals("c")){
      	System.out.print("Enter the Message : ");
      	String message = consoleScreen.readLine();

      //Sending message to the server
      	clientObj.socket.sendMessage(clientObj.serverSocket,message);
      	
      //Chat Operation is completed clientConsoleScreen is free now 
      	clientObj.consoleLock = false;
      }
      else if(clientOption.equals("e")) {
      //Sending message to the server
      	clientObj.socket.sendMessage(clientObj.serverSocket,"quit");
		System.out.println("Outcoming Closed");
      //Chat Operation is completed clientConsoleScreen is free now 
      	clientObj.consoleLock = false;
      
	  //Wait until newThread terminate its execution
		newThread.join();
      	System.out.println("Incoming Closed");
	  }
      else {
      //Chat Operation is completed clientConsoleScreen is free now 
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
	//If client Console Screen is busy. 
	  if(consoleLock) {
	  //Wait for client typing is completed.
	  	while(consoleLock) {

	  	}
	  }
	//Client Console screen is free now.
	  System.out.println("Server :"+message);
	 }while(!message.equals("quit"));


	}
}
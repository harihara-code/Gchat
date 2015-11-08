package Gchat.CLIENT;

import java.net.*;
import java.io.*;

public class Client implements Runnable{

    private Socket serverSocket = null;
    private Gchat.NetworkSocket socket = null;
    public static void main(String args[]) {
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
      


	}

	public void run() {
	 	
	}
}
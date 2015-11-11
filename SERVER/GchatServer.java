package Gchat.SERVER;

import java.io.*;
import java.net.*;

public class GchatServer {
    static final int serverPort = 7298;
	Gchat.NetworkSocket networkSocketObj;
	ServerSocket serverSocketObj;
	Gchat.SERVER.ClientHandler clientHandlerObj;
	int clientIndex;
	Socket tempSocket;
    

  //Constructing GchatServer object fields 
    public GchatServer() {
    	try {
    		serverSocketObj = new ServerSocket(serverPort);
		} catch(IOException ioe) {
			System.out.println("Error :"+ioe);
		}
    	networkSocketObj = new Gchat.NetworkSocket();
		clientHandlerObj = new ClientHandler(this);
		clientIndex = 0;
		tempSocket = null;
	
    }


	public static void main(String args[]) {
		GchatServer GchatServerObj = new GchatServer();

		do {
			try {
    		  //Accepting new client connection
				GchatServerObj.tempSocket = GchatServerObj.serverSocketObj.accept();
		 	} catch(IOException ioe) {
			System.out.println("Error :"+ioe);
			}
    		
		
		  //Allocating Resource for new client
		    if(GchatServerObj.clientHandlerObj.allocateResourceForClient(GchatServerObj.tempSocket,
		    															 GchatServerObj.clientIndex) ) {
		    	GchatServerObj.networkSocketObj.sendMessage(GchatServerObj.tempSocket,"Welcome Client Nice to connect you ...");
				GchatServerObj.clientHandlerObj.clientIncomingThread.start();


			} 
		    else {
		    	GchatServerObj.networkSocketObj.sendMessage(GchatServerObj.tempSocket,"Maximum client Limit Reached");
		    	try {
			     //Close this client connection
			       GchatServerObj.tempSocket.close();
				}catch(IOException ioe) {
					System.out.println("Error :"+ioe);
				}    
				GchatServerObj.tempSocket = null;

		    }

		} while(true);

	}
 
}
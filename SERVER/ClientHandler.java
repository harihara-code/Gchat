package Gchat.SERVER;

import java.net.*;
import java.io.*;

class ClientHandler implements Runnable{
	
	private static final int MAXIMUMCLIENT = 10;
    GchatClientSocket gchatClientSocketObj[];
	GchatServer gchatServerObj;
    Thread clientIncomingThread;

  //Constructuing ClientHandler Object fields
	public ClientHandler(GchatServer serverObj) {
		gchatClientSocketObj = new GchatClientSocket[MAXIMUMCLIENT];
		gchatServerObj = serverObj;
		clientIncomingThread = new Thread(this);
	}

  //Allocate resource for newly connected client
	public boolean allocateResourceForClient(Socket s,int socketID) {

		int resourceID = getFreeResourceIndex();

		if(resourceID == -1) 
			return false;
		else {
			gchatClientSocketObj[resourceID].socket = s;
			gchatClientSocketObj[resourceID].socketID = socketID;
			return true;
     	}

    }
    
   //Deallocate resource for existing connected client
	public boolean deallocateResourceForClient(int socketID) {
		for(int index = 0; index < 10; index++) {

		 // Client is disconnected so free this resource for reuse
			if(gchatClientSocketObj[index].socketID == socketID) {
				gchatClientSocketObj[index] = null;
				return true;
			}

		
		}
		return false;	
	}

  //Search for any free place available in gchatClientSocketObj[] if free it returns the resource index  
	public int getFreeResourceIndex() {
    	for(int index = 0; index < 10; index++) {

			if(gchatClientSocketObj[index] == null)	
				return index;
		}	

		return -1;
	}

  //get the client socket resource by specifying its client socket id 
	public GchatClientSocket getGchatClientSocketByID(int id) {
		for(int index = 0; index < 10; index++) {
			if(gchatClientSocketObj[index].socketID == id) {
				return gchatClientSocketObj[index];

			}

		}
		return null;
	}

  //Send this client message to all other connected clients
	public void sendToAll(String message,int gchatClientSocketID) {
		for(int index = 0; index < 10; index++) {
			if(gchatClientSocketObj[index] != null) {
			 //If other connected client available send this message 
				if(!(gchatClientSocketObj[index].socketID == gchatClientSocketID))
					gchatServerObj.networkSocketObj.sendMessage(gchatClientSocketObj[index].socket,message);
			}
		}
	}

	public void run() {
		incomingClientHandler();
	} 

 //Incoming Client Operation handle here
	public void incomingClientHandler() {
		GchatClientSocket clientGchatSocket = getGchatClientSocketByID(gchatServerObj.clientIndex);
		gchatServerObj.clientIndex++;
		String message = null;

	  do {		
	  //Receive message from connected client
		message = gchatServerObj.networkSocketObj.receiveMessage(clientGchatSocket.socket);
	  //Process the received client message
		if(!message.equals("quit")) {
			//this is valid client message so we sent this message to all other connected clients
			sendToAll(message,clientGchatSocket.socketID);
		}
		else {
		  try {
		  //If client want to disconnect from this server by sending quit signal
			clientGchatSocket.socket.close();	
		  } catch(IOException ioe) {
		  	System.out.println("Error :"+ioe);
		  }
			sendToAll(clientGchatSocket.socketID + " is disconnected from the server",clientGchatSocket.socketID);
			deallocateResourceForClient(clientGchatSocket.socketID);

		}
	  } while(message.equals("quit"));



	}
  

 }
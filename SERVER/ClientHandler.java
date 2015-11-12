///////////////////////////////////////////////////////////////////////////
/////File Name : ClientHandler.java////////////////////////////////////////
/////Coded by  : hariharan sathyanarayanan/////////////////////////////////
/////copyright @ 2015 <harihara95@gmail.com>///////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.SERVER;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable 
{
  //MAXIMUMCLIENTS represents the maximum client limit the server can handle
	private static final int MAXIMUMCLIENT = 10;

    GchatClientSocket gchatClientSocketObj[];

	GchatServer gchatServerObj;

    Thread clientIncomingThread[];

  //Constructing ClientHandler Object fields
	public ClientHandler(GchatServer serverObj) {
      //Initializing gchatClientSocketObj [] and clientIncomingThread[]
		gchatClientSocketObj = new GchatClientSocket[MAXIMUMCLIENT];
		clientIncomingThread = new Thread[MAXIMUMCLIENT];

		gchatServerObj = serverObj;

		for(int i = 0; i < MAXIMUMCLIENT; i++){
	
		 //Initializing gchatClientSocketObj[]
			gchatClientSocketObj[i] = new GchatClientSocket();
	
			gchatClientSocketObj[i].socket = null;
			clientIncomingThread[i] = null;

		}
	}

	//testing
		public ClientHandler() {
			gchatClientSocketObj = new GchatClientSocket[MAXIMUMCLIENT];
			clientIncomingThread = new Thread[MAXIMUMCLIENT];
		    for(int i = 0; i < MAXIMUMCLIENT; i++){
		 // Initializing gchatClientSocketObj[]
			gchatClientSocketObj[i] = new GchatClientSocket();
			gchatClientSocketObj[i].socket = null;
			clientIncomingThread[i] = null;

		 }
		
		}


  //Allocate resource for newly connected client
	public boolean allocateResourceForClient(Socket s,
											 int socketID,
											 String socketName) 
	{

		int resourceID = getFreeResourceIndex();

		//Debugging phase
			System.out.println("resource ID"+ resourceID);

		if(resourceID == -1) 
			return false;
		//if resourceID is valid 
		else {
			
			gchatClientSocketObj[resourceID].socket = s;
			gchatClientSocketObj[resourceID].socketID = socketID;
			gchatClientSocketObj[resourceID].socketName = socketName;
		//Notify other client that this client is connected to the server
			sendToAll(gchatClientSocketObj[resourceID].socketName+" is connected to the server",gchatClientSocketObj[resourceID].socketID);
			System.out.println(gchatClientSocketObj[resourceID].socketName + "is connected");
	   	// Allocate incomingthread
	        gchatServerObj.clientHandlerObj.clientIncomingThread[resourceID] = new Thread(gchatServerObj.clientHandlerObj);
	   	// start the new thread to receive message from the client
			gchatServerObj.clientHandlerObj.clientIncomingThread[resourceID].start();
			return true;
     	}

    }


	public void run() 
	{
		incomingClientHandler();
	} 

 //Incoming Client Operation handles here
	public void incomingClientHandler() {
       
      //Get newly connected client socket details  
		GchatClientSocket clientGchatSocket = getGchatClientSocketByID(gchatServerObj.clientIndex);

	  //checking clientGchatSocket == null
		if(clientGchatSocket == null) {
			System.out.println("Client Socket not found");
			return;
		}

	  //We safely used clientIndex to retrieve current client socket now we increment it without worry about that
		gchatServerObj.clientIndex++;
		
		String message = null;

    //Receive messages from this client and processing it  
	  do {		
	  	//We receive the message from connected client 
		   message = gchatServerObj.networkSocketObj.receiveMessage(clientGchatSocket.socket);
	  
   //Process the received client message
	 
		if(message.equals("whos")) 
		{
			String onlineList = "";
			System.out.println("whos request received");
			for(int i=0;i<MAXIMUMCLIENT;i++) 
			{
			   if(gchatClientSocketObj[i].socket != null) 
			   		onlineList += gchatClientSocketObj[i].socketName + " ";
			}
		
			if(onlineList == "") 
			{
			 	gchatServerObj.networkSocketObj.sendMessage(clientGchatSocket.socket,"None");

			}
			else
			{
				onlineList = "Online : "+onlineList;
				gchatServerObj.networkSocketObj.sendMessage(clientGchatSocket.socket,onlineList);

			}
			System.out.println("whos response sent");
		}

		else if(!message.equals("quit")) 
		{
		  //Message is attached with sender name 
			message = clientGchatSocket.socketName + ":"+message;
			//this is valid client message so we sent this message to all other connected clients
			  sendToAll(message,clientGchatSocket.socketID);
		}
		else 
		{
		  //System.out.println("Client Sent quit signal");
		 
		  try 
		  {
      	  //send disconnect signal to disconnect requesting client to close its receiving operation
             if(!gchatServerObj.networkSocketObj.sendMessage(clientGchatSocket.socket,"disconnect")) {
             	System.out.println("Message sending failed");
             }

		
		  //Client sent quit signal
			clientGchatSocket.socket.close();	
		  } catch(IOException ioe) {
		  	System.out.println("Error :"+ioe);
		  }
		  //client disconnected from server notification will send to all other connected clients
			sendToAll(clientGchatSocket.socketName + " is disconnected from the server",clientGchatSocket.socketID);
			System.out.println(clientGchatSocket.socketName + "is disconnected");
		 //Free this client allocated resource for reuse	
			deallocateResourceForClient(clientGchatSocket.socketID);

		}
      } while(!message.equals("quit"));
}
  
    
 //Deallocate resource for disconnected client
	public boolean deallocateResourceForClient(int socketID) 
	{
		for(int index = 0; index < MAXIMUMCLIENT; index++) {
		//Disconnected Client resource deallocation done here 
			if(gchatClientSocketObj[index].socketID == socketID) {
			  //only deallocate socket not gchatClientSocketObj
				gchatClientSocketObj[index].socket = null;
				return true;
			}

	    }

		return false;	
	}

  //Search for any free place available in gchatClientSocketObj[] if free it returns the resource index  
	public int getFreeResourceIndex() {
    	for(int index = 0; index < MAXIMUMCLIENT; index++) {

			if(gchatClientSocketObj[index].socket == null)	
				return index;
		}	

		return -1;
	}

  //get the client socket details by using client socket id  
	public GchatClientSocket getGchatClientSocketByID(int id) {

		for(int index = 0; index < MAXIMUMCLIENT; index++) {
		  //If searching clientSocket found 
			if(gchatClientSocketObj[index].socketID == id) {
				return gchatClientSocketObj[index];
			}
		}
		return null;
	}

  //Send the client message to all other connected clients
	public void sendToAll(String message,int gchatClientSocketID) {
		
		for(int index = 0; index < MAXIMUMCLIENT; index++) {
		
			if(gchatClientSocketObj[index].socket != null) {
			
			   //If other connected client available send this message 
			  	 if(! (gchatClientSocketObj[index].socketID == gchatClientSocketID) )
					gchatServerObj.networkSocketObj.sendMessage(gchatClientSocketObj[index].socket,message);
			}
		}
	}

}
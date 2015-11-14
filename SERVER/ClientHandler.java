///////////////////////////////////////////////////////////////////////////
//// File Name : ClientHandler.java          //////////////////////////////
//// Coded by  : hariharan sathyanarayanan   //////////////////////////////
//// Copyright @ 2015 <harihara95@gmail.com> //////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.SERVER;

import java.net.*;
import java.io.*;

//ClientHandler-GchatServer use this class to handle client connection
public class ClientHandler implements Runnable 
{
  	private static final int MAX_CLIENT_HANDLE = 10;
    GchatClientSocket GchatClientSocketObject[];
	GchatServer GchatServerObjectRef;
    Thread threadObject[];

  //ClientHandler Object Initialization
	public ClientHandler(GchatServer GchatServerObject)
	{
    	GchatClientSocketObject = new GchatClientSocket[MAX_CLIENT_HANDLE];
		threadObject = new Thread[MAX_CLIENT_HANDLE];
		GchatServerObjectRef = GchatServerObject;
    	for(int i = 0; i < MAX_CLIENT_HANDLE; i++)
    	{
			GchatClientSocketObject[i] = new GchatClientSocket();
	    	GchatClientSocketObject[i].socket = null;
			threadObject[i] = null;
		}
	}

  //Client Resource Allocation
	public boolean resourceAllocation(Socket s,int socketID,String socketName) 
	{
		int resourceID = getAvailableResourceIndex();
    	if(resourceID == -1) 
	    	return false;
		else 
		{
			GchatClientSocketObject[resourceID].socket = s;
			GchatClientSocketObject[resourceID].socketID = socketID;
			GchatClientSocketObject[resourceID].socketName = socketName;
		  
		  //ClientConnected Notification will send to all other connected clients
			sendToOtherClients("GchatServer : "+
								GchatClientSocketObject[resourceID].socketName+
								" is connected to the server",
								GchatClientSocketObject[resourceID].socketID);
		  //Start new thread to receive client messages
	        GchatServerObjectRef.ClientHandlerObject.threadObject[resourceID] = new Thread(GchatServerObjectRef.ClientHandlerObject);
	        GchatServerObjectRef.ClientHandlerObject.threadObject[resourceID].start();
			return true;
     	}

    }

  //Client Resource Deallocation
	public boolean resourceDeallocation(int socketID) 
	{
		for(int index = 0; index < MAX_CLIENT_HANDLE; index++) 
		{
			if(GchatClientSocketObject[index].socketID == socketID) 
			{
				GchatClientSocketObject[index].socket = null;
				return true;
			}
	    }
		return false;	
	}

  //Get the Available Resource [GchatClientSocketObject[]] Index
	public int getAvailableResourceIndex() 
	{
    	for(int index = 0; index < MAX_CLIENT_HANDLE; index++) 
    	{
			if(GchatClientSocketObject[index].socket == null)	
				return index;
		}	
		return -1;
	}


  //Receive Message from client
	public void run() 
	{
    	GchatClientSocket clientGchatSocket = getGchatClientSocketObjectByID(GchatServerObjectRef.clientCounter);
   		GchatServerObjectRef.clientCounter++;
		String message = "";
        do 
        {		
         //Receive message from the client
	  	   message = GchatServerObjectRef.NetworkSocketObject.receiveMessage(clientGchatSocket.socket);
	  	 //Processing client message and take appropriate action
	       if(message.equals("whos")) 
		   {
				String onlineList = "";
		     	for(int i=0;i<MAX_CLIENT_HANDLE;i++) 
				{
				   if(GchatClientSocketObject[i].socket != null) 
				   		onlineList += GchatClientSocketObject[i].socketName + " ";
				}
		    	onlineList = "Who's Online : "+onlineList;
		      //Send who's Online list information to the client
				GchatServerObjectRef.NetworkSocketObject.sendMessage(clientGchatSocket.socket,onlineList);
			}
            else if(message.equals("quit")) 
	    	{
		        try 
		        {
      	          //quit signal received from client 
                    GchatServerObjectRef.NetworkSocketObject.sendMessage(clientGchatSocket.socket,
                    													 "disconnect");
        		  //Close the disconnect client socket
        			clientGchatSocket.socket.close();	
                }
                catch(IOException ioe) 
                {
 		  			System.out.println("Error :"+ioe);
		  		}
		  	  //GchatServer send clientdisconnected notification to all other connected clients
     			sendToOtherClients("GchatServer : "+
     				                clientGchatSocket.socketName +
     				                " is disconnected from the server",
     				                clientGchatSocket.socketID);
     		  //Resource Deallocation for disconnected client
		    	resourceDeallocation(clientGchatSocket.socketID);
       		}
       		else
       		{
    	      //Message is attached with sender's name 
			    message = clientGchatSocket.socketName + " : "+message;
			  //GchatServer send this client message to all other connected clients
			    sendToOtherClients(message,clientGchatSocket.socketID);
	    	}
      }while(!message.equals("quit"));

		
	} 
  
  
  //Get GhatClientSocketObject through its socketID
    public GchatClientSocket getGchatClientSocketObjectByID(int id) 
    {
		for(int index = 0; index < MAX_CLIENT_HANDLE; index++) 
		{
			if(GchatClientSocketObject[index].socketID == id) 
			{
			  //GchatClientSocketObject found
				return GchatClientSocketObject[index];
			}
		}
		return null;
	}

  //GchatServer send message to all other connected clients
	public void sendToOtherClients(String message,int ID) 
	{
		for(int index = 0; index < MAX_CLIENT_HANDLE; index++) 
		{
			if(GchatClientSocketObject[index].socket != null) 
			{
		    	 if(GchatClientSocketObject[index].socketID != ID)
					GchatServerObjectRef.NetworkSocketObject.sendMessage(GchatClientSocketObject[index].socket,
																	     message);
			}
		}
	}
}
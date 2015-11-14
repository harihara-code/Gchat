///////////////////////////////////////////////////////////////////////////
//// File Name : GchatServer.java            //////////////////////////////
//// Coded by  : hariharan sathyanarayanan   //////////////////////////////
//// Copyright @ 2015 <harihara95@gmail.com> //////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.SERVER;

import java.io.*;
import java.net.*;

//GchatServer-Server program used to connect clients and transfer data between connected clients
public class GchatServer 
{
	static final int GchatServerPort = 7298;
	Gchat.NetworkSocket NetworkSocketObject;
	Gchat.SERVER.ClientHandler ClientHandlerObject;
	ServerSocket serverSocketObject;
	Socket clientSocket;
	int clientCounter;
 
  //GchatServer Object Initialization 
    public GchatServer() 
    {
       try 
       {
		 serverSocketObject = new ServerSocket(GchatServerPort);
	   } catch(IOException ioe) {
			System.out.println("Error :"+ioe);
		}
    	NetworkSocketObject = new Gchat.NetworkSocket();
		ClientHandlerObject = new ClientHandler(this);
		clientCounter = 0;
		clientSocket = null;
	}

    public static void main(String args[]) 
    {
		GchatServer GchatServerObject = new GchatServer();
		String clientName = null;
   	    System.out.println("GchatServer Started");
		do 
		{
			try
			{
   	    	  //Waiting for new client connection
				GchatServerObject.clientSocket = GchatServerObject.serverSocketObject.accept();
		 	} 
		 	catch(IOException ioe) 
		 	{
				System.out.println("Error :"+ioe);
			}
      	  //Get the name of the client
    		clientName = GchatServerObject.NetworkSocketObject.receiveMessage(GchatServerObject.clientSocket);
		  //Resource allocation for new client
		    if(GchatServerObject.ClientHandlerObject.resourceAllocation(GchatServerObject.clientSocket,
		    			                                                GchatServerObject.clientCounter,
		    								                            clientName)) 
			{
		        GchatServerObject.NetworkSocketObject.sendMessage(GchatServerObject.clientSocket,
		      	                                         "Welcome " +clientName+ " nice to connect you ...");
			}
    		else
			{
		     	GchatServerObject.NetworkSocketObject.sendMessage(GchatServerObject.clientSocket,
		     													  "Client handle limit exceeded");
		    	try 
		   		{
			     //Close this client connection
			       GchatServerObject.clientSocket.close();
				}catch(IOException ioe) {
					System.out.println("Error :"+ioe);
				}    
				GchatServerObject.clientSocket = null;
		    }
     	}while(true);
	}
 }
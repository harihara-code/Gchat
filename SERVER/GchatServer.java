///////////////////////////////////////////////////////////////////////////
//// File Name : GchatServer.java            //////////////////////////////
//// Coded by  : hariharan sathyanarayanan   //////////////////////////////
//// Copyright @ 2015 <harihara95@gmail.com> //////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.SERVER;

import java.io.*;
import java.net.*;

//GchatServer-Server program used to connect client and transfer data between connected clients
public class GchatServer 
{
	static final int serverPort = 7298;
	Gchat.NetworkSocket networkSocketObj;
	ServerSocket serverSocketObj;
	Gchat.SERVER.ClientHandler clientHandlerObj;
	int clientIndex;
	Socket tempSocket;
 
  //GchatServer Constructor to initialize its object member fields 
    public GchatServer() 
    {
       try 
       {
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
		String clientName = null;
   	//Debugging phase
   	    System.out.println("GchatServer Started");
		 		
 
		do 
		{
			try
			{
   		  //Accepting new client connection
				GchatServerObj.tempSocket = GchatServerObj.serverSocketObj.accept();
				//Debugging phase
				  //System.out.println("new Client Connected");
		 	} 
		 	catch(IOException ioe) 
		 	{
				System.out.println("Error :"+ioe);
			}
    		
    	//Get the name of the client
    		clientName = GchatServerObj.networkSocketObj.receiveMessage(GchatServerObj.tempSocket);
		
		  //Allocating Resource for the newly connected client
		    if(GchatServerObj.clientHandlerObj.allocateResourceForClient(GchatServerObj.tempSocket,
		    															 GchatServerObj.clientIndex,
		    															 clientName) ) 
		    {
		    	//Debugging phase
		    	 // System.out.println("Allocation successfull");
		    	  GchatServerObj.networkSocketObj.sendMessage(GchatServerObj.tempSocket,"Welcome " +clientName+ " nice to connect you ...");
    		   	
				


			} 
			else
			{
		    
		    	GchatServerObj.networkSocketObj.sendMessage(GchatServerObj.tempSocket,"Maximum client Limit Reached");
		   		try 
		   		{
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
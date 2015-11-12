package Gchat.TESTING;

import java.net.*;
import java.io.*;

class TestServer {
	public static void main(String args[]) throws Exception {
		Gchat.NetworkSocket socket = new Gchat.NetworkSocket();
		ServerSocket server = socket.createServerSocket(7298);
		Socket client = server.accept();
	    if(socket.receiveMessage(client).equals("quit\n")) 
	    	System.out.println("quit");
	    
	    else 
	    	System.out.println("quit/n");
		socket.sendMessage(client,"Welcome Client!! This is Gchat Server Program Application.\n");
		//System.out.println(socket.receiveMessage(client));
		Thread.currentThread().sleep(1000);
		socket.sendMessage(client,"Welcome Client!! This is Gchat Server Program Application.\n");
		

	}
}
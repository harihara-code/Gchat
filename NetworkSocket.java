package Gchat;

import java.net.*;
import java.io.*;

public class NetworkSocket implements Gchat.NetworkInterface {
	public Socket createSocket(String server,int port) {
		Socket socket = null;
		try{
			socket = new Socket(server,port);
		}catch(UnknownHostException exception){
			System.out.println("Error : "+exception);
		 }catch(IOException ioe){
			System.out.println("Error :"+ioe);
	     }
		return socket;
	}

	public ServerSocket createServerSocket(int port) {
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
		}
		catch(UnknownHostException exception){
			System.out.println("Error :"+exception);
	     }  
		
		catch(IOException ioe){
			System.out.println("Error :"+ioe);
	     }
	    return server;
	}

	public boolean sendMessage(Socket socket,String message) {
		try {
		  	PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(message);
			out.close();
		}catch(IOException ioe){
			System.out.println("Error :"+ioe);
		 }
		return true;
    }

    public String receiveMessage(Socket socket) {

    	return null;
    }

}
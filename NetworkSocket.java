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
			System.exit(-1);
		 }catch(IOException ioe){
			System.out.println("Error :"+ioe);
			System.exit(-1);
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
		    System.exit(-1);
	     }  
		
		catch(IOException ioe){
			System.out.println("Error :"+ioe);
			System.exit(-1);

	     }
	    return server;
	}

	public boolean sendMessage(Socket socket,String message) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
		  //	PrintWriter out = new PrintWriter(socket.getOutputStream());
		  	out.write(message);
	      	out.flush();
		  	out.close();
		}catch(IOException ioe){
			System.out.println("Error :"+ioe);
			System.exit(-1);

		 }
		return true;
    }

    public String receiveMessage(Socket socket) {
    	String message = null;
   
    	try {
		 	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = in.readLine();
			in.close();
		} catch (IOException ioe) {
			System.out.println("Error : "+ioe);
			System.exit(-1);

		  }
    	return message;
    }

}
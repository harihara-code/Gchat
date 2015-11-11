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

		OutputStreamWriter out = null;
		
		try {

			out = new OutputStreamWriter(socket.getOutputStream());
			message += "\n";
			out.write(message);
	      	out.flush();

		}catch(IOException ioe){

			System.out.println("Error :"+ioe);
		 //	System.exit(-1);
			return false;
		 }
	//Close the socket InputStream
		out.close();
         
		return true;

    }

    public String receiveMessage(Socket socket) {

    	String message = null;
   
    	try {

    	//Open the socket InputStream for data reading from it
		 	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = in.readLine();

		} catch (IOException ioe) {

			System.out.println("Error : "+ioe);
		    System.exit(-1);

		  }

		//Close the socket InputStream
		   in.close();

    	return message;

    }


}
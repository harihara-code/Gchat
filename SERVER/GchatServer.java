package Gchat.SERVER;

import java.io.*;
import java.net.*;

public class GchatServer {

	public static void main(String args[]) {

		static final int serverPort = 7298;
		NetworkSocket networkSocketObj = new networkSocketObj();
		ServerSocket serverSocketObj = new ServerSocketObj(serverPort);
		ClientHandler clientHandlerObj = new ClientHandler();
		int clientIndex;

		Socket tempSocket = null;

		do {
		  //Accepting new client connection request
			tempSocket = serverSocketObj.accept();

		  //Allocating Resource for connected client
		    if(clientHandlerObj.allocateResourecForClient(tempSocket,clientIndex)) {

		    } 
		    else {

		    }

		} while(true);

	}
}
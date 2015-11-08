package Gchat.TESTING;

import java.net.*;
import java.io.*;

class TestServer {
	public static void main(String args[]) throws Exception {
		Gchat.NetworkSocket socket = new Gchat.NetworkSocket();
		ServerSocket server = socket.createServerSocket(7872);
		Socket client = server.accept();
		socket.sendMessage(client,"Welcome Client!! This is Gchat Server Program Application.\n");
		System.out.println(socket.receiveMessage(client));

	}
}
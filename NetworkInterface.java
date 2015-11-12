package Gchat;

import java.net.*;

public interface NetworkInterface {
   Socket createSocket(String server,int port);
   ServerSocket createServerSocket(int port);
   boolean sendMessage(Socket socket,String message);
   String receiveMessage(Socket socket);
}

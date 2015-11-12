///////////////////////////////////////////////////////////////////////////
/////File Name : TestingClientHandler.java////////////////////////////////////////
/////Coded by  : hariharan sathyanarayanan/////////////////////////////////
/////copyright @ 2015 <harihara95@gmail.com>///////////////////////////////
///////////////////////////////////////////////////////////////////////////

package Gchat.TESTING;
import java.net.*;
public class TestingClientHandler {
	public static void main(String args[]) {
		Gchat.SERVER.ClientHandler testobj = new Gchat.SERVER.ClientHandler();
		for(int i = 0;i<11;i++)
			testobj.allocateResourceForClient(new Socket(),i);
	}
}
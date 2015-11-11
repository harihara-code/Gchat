package Gchat.SERVER;

class ClientHandler {
	
	private static final int MAXIMUMCLIENT = 10;
	GchatClientSocket gchatClientSocketObj[];

	public ClientHandler() {

		gchatClientSocketObj = new GchatClientSocket[MAXIMUMCLIENT];


	}

   //Allocate resource for newly connected client
	public boolean allocateResourceForClient(Socket s,int socketID,) {

		int resourceID = getFreeResourceIndex();

		if(resourceID == -1) {
			
			return false;
		}
		else {
	
			gchatClientSocketObj[resourceID].socket = s;
			gchatClientSocketObj[resourceID].socketID = socketID;
			return true;
     	}

    }
    
   //Deallocate resource for existing connected client
	public boolean deallocateResourceForClient(int socketID) {


		for(int index = 0; index < 10; index++) {

		 // Client is disconnected so free this resource for reuse
			if(gchatClientSocketObj[index].socketID == socketID) {
				gchatClientSocketobj[index] = null;
				return true;
			}

			return false;

		}	

	}

  //Search for any free place available in gchatClientSocketObj[] if free it returns the resource index  
	public int getFreeResourceIndex() {
		
		for(int index = 0; index < 10; index++) {

			if(gchatClientSocketObj[index] == null)	
				return index;
		}	

		return -1;
	}






}
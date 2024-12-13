
import java.rmi.registry.*;

public class Server {

	/**
	 * Server which utilizes RMI to communicate with a client. RMI provides multi-threading.
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {  // get port number entered as an argument
				System.out.println("Enter port number. (Example: java Server 32000):");
				return;
			}
			
			int portNum = Integer.parseInt(args[0]);  // store port number of server
			
		try {
			// Create the remote object that is the key-value store
			KeyValueStoreRemoteInterface keyValueStore = new KeyValueStore(portNum);  //pass coordinator to key-val store
			
            // Create and bind the registry
            Registry registry = LocateRegistry.createRegistry(portNum);
            registry.rebind("store1", keyValueStore);  // "store1" is the identifier used by the client to look up the remote object which is the key-value store

            System.out.println("Server is connected on port " + portNum);
            
            // Create and bind the participant so that coordinator can find the participant
            Participant participant = new Participant(keyValueStore);
            registry.rebind("participant" + portNum, participant); // "participant" + portNum is the unique identifier for the participant    
            
            System.out.println("Server as participant has been registered.");
            
        } catch (Exception e) {
            System.out.println("Server exception: " + e.toString());
        }
	}
}

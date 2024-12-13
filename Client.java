
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A client which communicates with a server using RMI.
 */
public class Client {
	
	/**
	 * Runs the Client which will request operations (PUT, GET, DELETE) on a remote object holding a key-value store. 
	 * Receives IP and port number from command line inputs. 
	 * @param args
	 */
	public static void main (String args[]) {
		
		ClientLogger log = null;
		
		if (args.length != 2) {  // check if IP and port were entered as args
			System.out.println("Enter server IP, and port number. (Example: java Client 128.111.49.44 32000):");
			return;  // if incorrect, exits the program
		}
		
		String ipNum = args[0];  // store IP address/host name of server
		int portNum = Integer.parseInt(args[1]);  // store port number of server
		try{

			// Get the RMI registry
			Registry registry = LocateRegistry.getRegistry(ipNum, portNum);
			
			// Look up the remote object/ key-value store
			KeyValueStoreRemoteInterface keyValueStoreObjStub = (KeyValueStoreRemoteInterface) registry.lookup("store1");
			
			int clientID = keyValueStoreObjStub.calculateClientID();
			log = new ClientLogger(clientID, ipNum, portNum);  // instantiate a client log which will log every request to a server and response from a server
		
			// Send pre-populated key-value pairs to a method in the remote object to add the values to the key-value store
			String toSend = "key1:data1,key2:data2,key3:data3,key4:data4,key5:data5";
			keyValueStoreObjStub.convertStringToHashMap(clientID, ipNum, portNum, toSend);  // calls remote method, client identifiers included (ID,port,IP) included to differentiate between clients
			log.sendLog(toSend);  // log the data sent by the client to the client log
			
			// Send operations to be performed on the key-value store
	        OperationsList opsList = new OperationsList();  // object containing list of operations to be performed
	        
	        for (String operation : opsList.getOperationsList()) {  // loop through the array containing the operations
	            try {
	        	if (operation.equals("exit")) {  // No more operations to perform, break out of loop so as to stop reading operation list
	        		toSend = "exit";
	        		log.sendLog(toSend);
	        		break;  // exit the program
	        	}
	        	
	        	// Send the operation (PUT,GET,DELETE) to the remote method in the key-value store remote object to perform modifications to the key-value store
	        	String returnValue = keyValueStoreObjStub.modifyStore(clientID, ipNum, portNum, operation);
	            log.sendLog(operation);  // record the sent operation
	            log.receiveLog(returnValue);  // record the received result(returnValue) after sending the operation
	          
	            }
	            catch (Exception e) {
	            	log.errorLog("Operation not successful");  // catches error in the loop going through the operations
	            }

	        }
	        
		} catch (Exception e){
			log.errorLog("Error"+ e.getMessage());}
	}
}
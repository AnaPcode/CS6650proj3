
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class contains the key-value store as well as the methods to modify the store.
 */
public class KeyValueStore extends UnicastRemoteObject implements KeyValueStoreRemoteInterface {
    private ConcurrentHashMap<String, String> map;  // key-value store
    private ServerLogger serverLog;  // records all actions performed on the key-value store

	private static int numClients = 0;  // number of clients utilized to assign unique identifier to clients
	
	private int portNum;  // port number of current server, will be utilized by coordinator to assume other server instances names in registry.
	
    /**
     * Constructor instantiates the map.
     * @param coordinator 
     */
    public KeyValueStore(int portNum) throws RemoteException {
    	this.map = new ConcurrentHashMap<>();
    	this.portNum = portNum;
    }
    
    /**
	 * Helper method to assign unique identifier when there are multiple clients
	 * Example, for the first client, calculates this clients ID as numClients (0) + 1 = 1, the second client would then be 2, and so forth.
	 * @return newClientID
	 */
	@Override
	public synchronized int calculateClientID() {  // synchronized so as to handle multiple exclusion since multiple clients may access the method at the same time
		numClients++;
		return numClients;
	}

    /**
     * Method splits a String of key-value pairs in the format "key1:key1,key2:key2" and places the pairs into the hashmap.
     * @param input
     * @return
     */
    @Override
	public synchronized void convertStringToHashMap(int clientID, String ipNum, int port, String values) throws RemoteException {
        
    	this.serverLog = new ServerLogger(ipNum, port, clientID);
    	
    	serverLog.receiveLog(values);  // log string of key-pair values received from the client
    	
        String[] keyValuePairs = values.split(",");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
    }
    
    
    // split up modify store so if server is participant and commit is approved, then can just go to executeOps and not go through 2PC which could lead to indefinite loop.
    /**
	 * Method which parses the operation specified by the client to find action to be performed, triggers 2PC if a put or delete request received. 
	 * And then calls executeOps method to perform the put, get, or delete methods to execute the operations.
	 * @param operation
	 * @return
	 */
    @Override
    public synchronized String modifyStore(int clientID, String ipNum, int port, String operation) throws RemoteException {
    	
    	serverLog.updateClient(clientID, ipNum, port);
    	
    	serverLog.receiveLog(operation);  // log operation received from client
    	
        String[] parts = operation.split(" ");
        String action = parts[0];  // specifies whether to put, get, or delete
        String output;
        
        // added for 2PC
        // Check with Coordinator for 'put' or 'delete' actions
        if (action.equals("put") || action.equals("delete")) {
            Coordinator coordinator = new Coordinator(portNum);  // create coordinator to start 2PC
            
            boolean canCommit = coordinator.twoPhaseCommitEngine(clientID, ipNum, port, operation);  // call method that checks with other servers
            
            // If canCommit returns false, then cannot commit
            if (!canCommit) {
                output = "canCommit was false, commit not approved in 2PC";
                serverLog.sendLog(output);
                return output;
            }
        }
        
        String result = executeOps(clientID, ipNum, port, operation);  // proceed with committing changes
        return result;  // returns result to client
    }
    
    
    
	/**
	 * Method which parses the operation specified by the client to find action to be performed and then calls the put, get, or delete methods to execute the operation.
	 * @param operation
	 * @return
	 */
    @Override
    public synchronized String executeOps(int clientID, String ipNum, int port, String operation) throws RemoteException {
    	
    	serverLog.updateClient(clientID, ipNum, port);
    	
    	//serverLog.receiveLog(operation);  // log operation received from client
    	
        String[] parts = operation.split(" ");
        String action = parts[0];  // specifies whether to put, get, or delete
        String output;
        
        
        switch (action.toLowerCase()) {
            case "put":
                if (parts.length == 3) {  // put key value
                    String key = parts[1];
                    String value = parts[2];
                    output = put(key, value);  // call put method to place key-value pair into the map
                } 
                else {
                    output = "Error. Not able to put key-value pair.";
                }
                break;

            case "get":
                if (parts.length == 2) {  // get key
                    String key = parts[1];
                    output = get(key);
                } 
                else {
                    output = "Error. Not able to get value";
                }
                break;

            case "delete":
                if (parts.length == 2) {  // delete key
                    String key = parts[1];
                    output = delete(key);
                } else {
                    output = "Error. Not able to delete";
                }
                break;

            default:
                output = "Error. Action not found.";
                break;
        }
        
        serverLog.sendLog(output);  // record message to be sent to the client
        
        System.out.println("\n" + getMap() + "\n");  // print current stare of key-value pairs store for error checking.
        
        return output;
    }

    /**
     * Method puts a key-value pair into the key-value store.
     * @param key
     * @return String message of the action performed
     */
    @Override
    public String put(String key, String value) throws RemoteException {
        map.put(key, value);
        return "Put: " + key + ":" + value + " into key-value store";
    }

    /**
     * Method gets and returns the value specified by a given key.
     * @param key
     * @return String message of the action performed
     */
    @Override
    public String get(String key) throws RemoteException {
        String value = map.get(key);
        if (value != null) {
            return "Value for " + key + " is: " + value;
        } 
        else {
            return "Key not found.";
        }
    }

    /**
     * Method deletes a key from the key-value store.
     * @param key
     * @return String message of the action performed
     */
    @Override
    public String delete(String key) throws RemoteException {
        if (map.remove(key) != null) {
            return "Deleted key-value pair with key:" + key;
        } 
        else {
            return "Key not found.";
        }
    }
    
    /**
     * Method which returns the map as a type String.
     * @return map
     */
    @Override
    public String getMap() throws RemoteException {
    	String mapOfTypeString = map.toString();
    	return mapOfTypeString;
    }
}

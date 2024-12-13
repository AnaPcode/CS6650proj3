import java.rmi.*;

/**
 * Remote interface that specifies the methods that can be called remotely.
 * Object implementing the interface can be accessed by remote clients.
 * 
 * Class contains the key-value store as well as the methods to modify the store.
 */
 
public interface KeyValueStoreRemoteInterface extends Remote {
	
	/**
	 * Helper method to assign unique identifier when there are multiple clients
	 * Example, for the first client, calculates this clients ID as numClients (0) + 1 = 1, the second client would then be 2, and so forth.
	 * @return newClientID
	 */
	public int calculateClientID() throws RemoteException;
	
    /**
     * Method splits a String of key-value pairs in the format "key1:key1,key2:key2" and places the pairs into the hashmap.
     * @param input
     * @return
     */
	public void convertStringToHashMap(int clientID, String ipNum, int port, String values) throws RemoteException;
	
	/**
	 * Method which parses the operation specified by the client to find action to be performed and then calls the put, get, or delete methods to execute the operation.
	 * @param operation
	 * @return
	 */
    public String modifyStore(int clientID, String ipNum, int port, String operation) throws RemoteException;

    /**
     * Method puts a key-value pair into the key-value store.
     * @param key
     * @return String message of the action performed
     */
    public String put(String key, String value) throws RemoteException;
    
    /**
     * Method gets and returns the value specified by a given key.
     * @param key
     * @return String message of the action performed
     */
    public String get(String key) throws RemoteException;

    /**
     * Method deletes a key from the key-value store.
     * @param key
     * @return String message of the action performed
     */
    public String delete(String key) throws RemoteException;
    
    /**
     * Method which returns the map as a type String.
     * @return map
     */
    public String getMap() throws RemoteException;

	/**
	 * Method which parses the operation specified by the client to find action to be performed and then calls the put, get, or delete methods to execute the operation.
	 * @param operation
	 * @return
	 */
	String executeOps(int clientID, String ipNum, int port, String operation) throws RemoteException;
    
}
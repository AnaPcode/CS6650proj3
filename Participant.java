import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class for 2PC. Coordinator asks Participant if a commit can be made, participant will reply to coordinator. If true, then coordinator and participant will commit the change.
 */
public class Participant extends UnicastRemoteObject implements ParticipantRemoteInterface {

	public KeyValueStoreRemoteInterface keyValStore;
	
	public Participant(KeyValueStoreRemoteInterface keyValStore) throws RemoteException {
		super();
		this.keyValStore = keyValStore;
	}

	/**
	 * Checks if participant can commit. Participant will return true if coordinator and participant successfully connected. Additional checks can be implemented in the future.
	 */
	@Override
	public boolean prepare() throws RemoteException {
		return true;
	}
	
	/**
	 * Commits the change to the key value store.
	 */
	@Override
	public void commit(int clientID, String ipNum, int port, String operation) throws RemoteException {
        try {
            keyValStore.executeOps(clientID, ipNum, port, operation); // Calls method to modify the current server's key value store.
        } catch (Exception e) {
            System.out.println("Could not commit: " + e.getMessage());
        }
	}
	
	/**
	 * Method executed when change can not be committed.
	 */
	@Override
	public void rollback() throws RemoteException {
		System.out.println("Rollback, no changes made to keyValStore");
	}
	
}

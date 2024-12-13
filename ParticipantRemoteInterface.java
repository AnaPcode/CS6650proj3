import java.rmi.*;

public interface ParticipantRemoteInterface extends Remote {
	
	boolean prepare() throws RemoteException;

    void commit(int clientID, String ipNum, int port, String operation) throws RemoteException;

    void rollback() throws RemoteException;
    
}

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Coordinator {
	
    ArrayList<ParticipantRemoteInterface> participants = new ArrayList<>();

    public Coordinator(int portNum) throws RemoteException {
		// infer names of participants to be able to look up in RMI registry. There will be 4 participants(other servers), with port numbers ranging from 3001-3005 inclusive.
    	// names given to participants follow "participant" + portNum therefore, if portNum passed into this constructor is 3002,
    	// then ignore 3002 since this is the port number of the coordinator (current server) and the other participants names can be inferred to then as
    	// participant3001m, participant3003, participant3004, participant3005
    	for (int i = 3001; i <= 3005; i++) {
    		if (i != portNum) {
    			String participantName = "participant" + i;
    			try {
                     Registry registry = LocateRegistry.getRegistry("server" + (portNum - 3000), i);  // get registry
                     ParticipantRemoteInterface participant = (ParticipantRemoteInterface) registry.lookup(participantName);  // look up participant
                     addParticipant(participant);  // add participant to list
                 } catch (Exception e) {
                     System.out.println("Error connecting to participant " + participantName + " on port " + i + ": " + e.getMessage());
                 }
             }
         }
     }  	

    /**
    * Implements two phase commit protocol.
 	* @param clientID
 	* @param ipNum
 	* @param port
 	* @param operation
 	* @return
 	* @throws RemoteException
 	*/
	public boolean twoPhaseCommitEngine(int clientID, String ipNum, int port, String operation) throws RemoteException {
        
    	if (participants.size() == 0) {
    		return true;
    	}
    	
    	// Prepare, check if other servers are able to commit
        for (ParticipantRemoteInterface participant : participants) {
            if (!participant.prepare()) {
                abortRequest();
                return false;
            }
        }

        // Commit
        for (ParticipantRemoteInterface participant : participants) {
        	participant.commit(clientID, ipNum, port, operation);
        }
        return true;
    }

    // Abort request
    private void abortRequest() throws RemoteException {
        for (ParticipantRemoteInterface participant : participants) {
            participant.rollback();
        }
    }
	
    // Method to add participants
    public void addParticipant(ParticipantRemoteInterface participant) throws RemoteException {
        participants.add(participant);
    }

	
}

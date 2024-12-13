
/**
 * Class extends the abstract class Logger.
 * Logs requests from the client and responses sent to the client from the server side.
 * Contains synchronized methods in case multiple clients log at the same time.
 */
public class ServerLogger extends Logger {

    private String clientIp;
    private int clientPort;
    private int clientID;

    /**
     * Constructor to initialize client IP and port, and client ID
     * @param clientIp
     * @param clientPort
     * @param clientID
     */
    public ServerLogger(String clientIp, int clientPort, int clientID) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.clientID = clientID;
    }
    
    /**
     * Method which updates the client's ID, port, and IP. Called upon when a client calls modifyStore method.
     * Resolves the issue of the log printing the incorrect client ID.
     * @param clientID
     * @param ipNum
     * @param port
     */
    public synchronized void updateClient(int clientID, String ipNum, int port) {
    	this.clientIp = ipNum;
    	this.clientPort = port;
    	this.clientID = clientID;
    }
	
    @Override
	public synchronized void receiveLog(String message) {  // Prints message/request that was received from client over the network
		System.out.println(Logger.captureTime() + " Received from Client" + clientID +  "<" + clientIp + ">:<" + clientPort + ">   " + message);
	}

	@Override
	public synchronized void sendLog(String message) {  // Prints the message that was sent to the client over the network
		System.out.println(Logger.captureTime() + " Responded to Client" + clientID + "<" + clientIp + ">:<" + clientPort + ">    " + message);
	}

	@Override
	public synchronized void errorLog(String message) {  // Prints error message
		System.out.println(Logger.captureTime() + " Error with Client" + clientID + "<" + clientIp + ">:<" + clientPort + ">      " + message);
	}
	
}

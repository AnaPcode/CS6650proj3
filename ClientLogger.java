
/**
 * Class extends the abstract class Logger.
 * Logs responses from the server and requests sent to the server from the client side.
 * Contains synchronized methods in case multiple clients log at the same time.
 */
public class ClientLogger extends Logger {

	private int clientID;
    private String serverIp;
    private int serverPort;

    /**
     * Constructor to initialize server IP and port.
     * @param clientID
     * @param serverIp
     * @param serverPort
     */
    public ClientLogger(int clientID, String serverIp, int serverPort) {
        this.clientID = clientID;
    	this.serverIp = serverIp;
        this.serverPort = serverPort;
    }
	
    @Override
	public synchronized void receiveLog(String message) {  // Prints message that was received from the server
		System.out.println(Logger.captureTime() + " Client" + clientID + " Received from Server<" + serverIp + ">:<" + serverPort + ">  " + message);
	}

	@Override
	public synchronized void sendLog(String message) {  // Prints message that was sent to the server
		System.out.println(Logger.captureTime() + " Client" + clientID + " Request to Server<" + serverIp + ">:<" + serverPort + ">     " + message);
	}

	@Override
	public synchronized void errorLog(String message) {  // Prints error message
		System.out.println(Logger.captureTime() + " Client" + clientID + " Error with Server<" + serverIp + ">:<" + serverPort + ">     " + message);
	}

}

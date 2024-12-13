import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An abstract class which contains methods related to logging receive, reply, and error messages between a server and a client.
 */
public abstract class Logger {

    /**
     * Method gets current system time in milliseconds and returns the time formatted to dd MM yyyy HH:mm:ss:SSS Z.
     * @return the current time formatted as a type String
     */
    public static String captureTime() {
        long currentTimeMillis = System.currentTimeMillis();  // get current system time
        
        // format the time
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        Date formattedTime = new Date(currentTimeMillis);
 
        return sdf.format(formattedTime);  // return formatted time
    }
    
    /**
     * Method calls captureTime() method to get the time and prints to console the message that was received over the network.
     * @param message
     */
    public abstract void receiveLog(String message);
    
    /**
     * Method calls captureTime() method to get the time and prints to the console the message that was sent over the network.
     * @param message
     */
    public abstract void sendLog(String message);
    
    /**
     * Method calls captureTime() method to get the time and prints to the console error messages.
     * @param message
     */
    public abstract void errorLog(String message);

}

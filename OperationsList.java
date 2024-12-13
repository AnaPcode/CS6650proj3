
import java.util.ArrayList;

/**
 * Class creates and lists the operations to be sent by the client to the server to be executed on the key-value store
 */
public class OperationsList {
    private ArrayList<String> operations;  // list of operations

    /**
     * Constructor which creates an array list and populates the list with operations
     */
    public OperationsList() {
        operations = new ArrayList<>();
        
        // PUT (key, value) operations
        operations.add("put key25 value25");
        operations.add("put key55 value55");
        operations.add("put key1 value100");
        operations.add("put key123 value123");
        operations.add("put key166 value166");
        operations.add("put key166");  // should return error
        
        
        // GET (key) operations
        operations.add("get key1");
        operations.add("get key166");
        operations.add("get key3");
        operations.add("get key5");
        operations.add("get key100");
        operations.add("get key5000");  // should return no key-value found
        operations.add("get");  // no key specified
        
        
        // DELETE (key) operations
        operations.add("delete key1");
        operations.add("delete key3");
        operations.add("delete key123");
        operations.add("delete key4");
        operations.add("delete key100");
        operations.add("delete key100");  // should return error since key has already been deleted
        
      
        // EXIT (no more operation requests)
        operations.add("exit");
    }

    /**
     * Getter method so that the Client classes can get the list of operations.
     * @return list of operations
     */
    public ArrayList<String> getOperationsList() {
        return operations;
    }

}
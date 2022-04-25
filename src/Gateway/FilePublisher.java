package Gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that manager the observers, which are the listeners. It is the basic publisher that subscribes and unsubscribes
 * listeners for each action type when needed and contains notify method to let the listeners update by specifying
 * the action type.
 *
 * listeners: a HashMap where key is the action type(operation), value is the ArrayList of corresponding listener(s).
 *
 * @author Zhen Cheng
 * @author Qing Lyu
 */
public class FilePublisher {
	private HashMap<String, ArrayList<FileListener>> listeners;

	/**
	 * Constructor for FilePublisher object. It initialize listeners attribute and
	 * put certain operations into the hashmap
	 *
	 * @param operations arraylist of action operations
	 */
	public FilePublisher(ArrayList<String> operations) {
		listeners = new HashMap<>();
		for (String operation: operations) {
			this.listeners.put(operation, new ArrayList<>());
		}
	}

	/**
	 * Add the observer(listener) into the corresponding ArrayList in listeners by passing in eventType, which is
	 * the key in HashMap listeners.
	 *
	 * @param eventType save/read what type of file
	 * @param listener observers
	 */
	public void subscribe(String eventType, FileListener listener) {
		ArrayList<FileListener> users = listeners.get(eventType);
		users.add(listener);
	}

	/**
	 * remove the observer(listener) into the corresponding ArrayList in listeners by passing in eventType, which is
	 * the key in HashMap listeners.
	 *
	 * @param eventType save/read what type of file
	 * @param listener observers
	 */
	public void unsubscribe(String eventType, FileListener listener) {
		ArrayList<FileListener> users = listeners.get(eventType);
		users.remove(listener);
	}

	/**
	 * Notify the change to observers for the specific actionType by calling their update method
	 *
	 * @param actionType save/read what type of file
	 * @throws IOException exception occurs when we call update method
	 */
	public void notify(String actionType) throws IOException {
		ArrayList<FileListener> users = listeners.get(actionType);
		for (FileListener listener : users) {
			listener.update(actionType);
		}
	}
}

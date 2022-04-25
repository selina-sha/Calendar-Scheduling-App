package Controller;

import Entity.Message;
import UseCase.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that manager the observers, which are the listeners. It is the basic publisher that subscribes
 * listeners for each action type when needed and contains notify method to let the listeners update by specifying
 * the action type.
 *
 * listeners: a HashMap where key is the action type(operation), value is the ArrayList of corresponding listener(s).
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class MessagePublisher {
	private HashMap<String, ArrayList<MessageListener>> listeners;

	/**
	 * Constructor for MessagePublisher object. It initialize listeners attribute and
	 * put certain operations into the hashmap
	 * @param operations arraylist of action operations
	 */
	public MessagePublisher(ArrayList<String> operations) {
		this.listeners = new HashMap<>();
		for (String operation: operations) {
			this.listeners.put(operation, new ArrayList<>());
		}
	}

	/**
	 * Add the observers(listener) into the corresponding ArrayList in listeners by passing in eventType, which is
	 * the key in HashMap listeners.
	 * @param eventType the action type, which is the key in listeners
	 * @param listener the listener to be added
	 */
	public void subscribe(String eventType, MessageListener listener) {
		ArrayList<MessageListener> users = listeners.get(eventType);
		users.add(listener);
	}

	/**
	 * Notify the change to observers for the specific actionType by calling their update method
	 * @param actionType the key in listeners, and the way of sending message we want when calling
	 * @param message the message to be updated
	 * @throws UserNotFoundException when calling update
	 */
	public void notify(String actionType, Message message) throws UserNotFoundException {
		ArrayList<MessageListener> users = listeners.get(actionType);
		for (MessageListener listener : users) {
			listener.update(actionType, message);
		}
	}
}

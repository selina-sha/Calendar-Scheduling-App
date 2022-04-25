package Controller;

import Entity.Message;
import UseCase.UserNotFoundException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A observable class for send message between users.
 * It has one attribute, events.
 * When writing this class, we looked at this web:
 * https://refactoring.guru/design-patterns/observer/java/example
 * events: A MessagePublisher object
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class MessageSender {
	public MessagePublisher events;

	/**
	 * Constructor for MessageSender object. It initialize events attribute by
	 * passing in the operations ArrayList, which contains:
	 * regularToAmin(regular user send message to admin common inbox),
	 * regularToOther(regular user send message to other user except for admin),
	 * adminToAll(admin send message to all other user),
	 * adminToOther(admin send message to some users(one or more))
	 *
	 * @param operations arraylist of action operations
	 */
	public MessageSender(ArrayList<String> operations) {
		this.events = new MessagePublisher(operations);
	}

	/**
	 * Notify observers to send messages according to the passed in action type and message
	 * @param actionType is the way of sending this message
	 * @param message the message to be sent
	 * @throws IOException when we call notify method
	 * @throws UserNotFoundException when we call notify method
	 */
	public void sendMsg(String actionType, Message message) throws IOException, UserNotFoundException {
		events.notify(actionType, message);
	}
}

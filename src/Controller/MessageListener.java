package Controller;

import Entity.Message;
import UseCase.UserNotFoundException;


/**
 * An interface that contains one method update, which is implemented by SendMsgListener.
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public interface MessageListener {
	/**
	 * called by notify method in MessagePublisher when there is any action (send message) happens.
	 * @param actionType is the way of sending message when this method is called
	 * @param message is the Message object
	 * @throws UserNotFoundException when the implemented methods throws exception
	 */
	void update(String actionType, Message message) throws UserNotFoundException;
}

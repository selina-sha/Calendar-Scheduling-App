package Controller;

import Entity.Message;
import UseCase.MessageManager;
import UseCase.UserNotFoundException;

/**
 * A observer class. It implements MessageListener interface.
 *
 * mm: MessageManager object
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class SendMsgListener implements MessageListener{
	private MessageManager mm;

	/**
	 * Constructor for SendMsgListener object. It initialize mm.
	 * @param mm the MessageManager object
	 */
	public SendMsgListener(MessageManager mm){
		this.mm = mm;
	}

	/**
	 * Override the method from its interface MessageListener.
	 * Update all the changes. (send message to specific receiver determined by actionType)
	 * @param actionType is the way of sending message when this method is called
	 * @param message is the Message object
	 * @throws UserNotFoundException when methods from mm throws when calling
	 */
	@Override
	public void update(String actionType, Message message) throws UserNotFoundException {
		if (actionType.equals("regularToAdmin")){
			mm.sendToAdmin(message);
		}else if (actionType.equals("regularToOther")){
			mm.sendMessage(message);
		}else if (actionType.equals("adminToAll")){
			mm.sendtoAllUser(message);
		}else if (actionType.equals("adminToOther")){
			mm.admintoSomeUsers(message);
		}
	}
}

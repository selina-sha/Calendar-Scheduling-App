package UseCase;

import Entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *  a class called MessageManager which is used to send, reply, delete and read messages by users.
 *  It has 3 attributes: um, chatHistory, commonInbox.
 *  um: a UserManager class
 *  chatHistory: a hashmap with key (userid) and value (user's messages chat history)
 *  commonInbox: an ArrayList stores the messages interact with admin users.
 *
 * @author Siqing Xu
 * @author Galaxy Zhang
 * @author Christine Chen
 *
 */
public class MessageManager {
    public UserManager um;
    private HashMap<String, ArrayList<Message>> chatHistory;
    private ArrayList<Message> commonInbox;


    /**
     * a constructor of the MessageManager.
     * It initialized the chatHistory and commonInbox.
     * @param um a UserManager
     */

    public MessageManager(UserManager um){
        this.um = um;
        this.commonInbox = new ArrayList<>();
        this.chatHistory = new HashMap<>();

    }

    /**
     * Create a new message with the attributes of a message: senderID, receiverID and content.
     * @param senderID
     * @param receiverID
     * @param content
     * @return a new message with the given attributes.
     */
    public Message createMessage(String senderID, String receiverID, String content){
        return new Message(senderID, receiverID,content);
    }

    /**
     * a setter of the commonInbox.
     * @param commonInbox takes the given parameter as the new commonInbox.
     */

    public void setCommonInbox(ArrayList<Message> commonInbox){
        this.commonInbox = commonInbox;
    }


    /**
     * a setter of the chatHistory
     * @param chatHistory takes the given parameter as the new chatHistory.
     */
    public void setChatHistory(HashMap<String, ArrayList<Message>> chatHistory) {
        this.chatHistory = chatHistory;
    }


    /**
     * a method to send a message to admin user and add the message into the commonInbox.
     * @param message the message used to sent.
     */

    public void sendToAdmin(Message message){
        ArrayList<Message> allMessages = getCommonInbox();
        allMessages.add(message);
        setCommonInbox(allMessages);
        if (chatHistory.isEmpty()){
            chatHistory = new HashMap<>();
            setChatHistory(chatHistory);
        }
        if (chatHistory.get(message.getSenderID()) == null){
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            chatHistory.put(message.getSenderID(),messages);
        } else {chatHistory.get(message.getSenderID()).add(message);}
        setChatHistory(chatHistory);
        }

    /**
     * a method used to send message from admin to some other users.
     * @param message the message used to sent.
     */

    public void admintoSomeUsers(Message message) {
        commonInbox.add(message);
        if(chatHistory.isEmpty()){
            chatHistory = new HashMap<>();
        }
        if (chatHistory.get(message.getReceiverID()) == null){
            ArrayList<Message> messages = new ArrayList<>();
            chatHistory.put(message.getReceiverID(), messages);
        }
        chatHistory.get(message.getReceiverID()).add(message);
    }

    /**
     * a method used to send a message from admin to all users.
     * @param message the message used to sent.
     */

    public void sendtoAllUser(Message message) {
        ArrayList<PermanentUser> pms = um.getPermanentUsers();
        for (PermanentUser pm : pms) {
            if (!pm.getUserType().equals("admin")){
                String userid = pm.getUserId();
                Message copy = makeMessageCopy(message);
                copy.setReceiverID(userid);
                commonInbox.add(copy);
                if (chatHistory.isEmpty()) {
                    chatHistory = new HashMap<>();
                }
                if (chatHistory.get(copy.getReceiverID()) == null) {
                    ArrayList<Message> messages = new ArrayList<>();
                    chatHistory.put(copy.getReceiverID(), messages);
                }
                chatHistory.get(copy.getReceiverID()).add(copy);
            }
        }
    }

    /**
     * a method used to send message to permanent users except admin.
     * @param message the message user want to sent.
     * @throws UserNotFoundException
     */

    public void sendMessage(Message message) throws UserNotFoundException{
        String senderid = message.getSenderID();
        String receiverid = message.getReceiverID();
        ArrayList<PermanentUser> pms = um.getPermanentUsers();
        ArrayList<String> userids = new ArrayList<>();
        for (PermanentUser pm : pms) {
            userids.add(pm.getUserId());
        }
        if (userids.contains(receiverid)) {
                if (chatHistory == null) {
                    chatHistory = new HashMap<>();
                }
                ArrayList<Message> sendermessage = chatHistory.get(senderid);
                ArrayList<Message> receivermessage = chatHistory.get(receiverid);
                if (sendermessage != null && receivermessage != null) {
                    sendermessage.add(message);
                    receivermessage.add(message);
                } else if (sendermessage != null) {
                    ArrayList<Message> messages = new ArrayList<>();
                    messages.add(message);
                    chatHistory.put(receiverid, messages);
                    sendermessage.add(message);
                } else if (receivermessage != null) {
                    ArrayList<Message> messages = new ArrayList<>();
                    messages.add(message);
                    chatHistory.put(senderid, messages);
                    receivermessage.add(message);
                } else {
                    ArrayList<Message> messages1 = new ArrayList<>();
                    ArrayList<Message> messages2 = new ArrayList<>();
                    messages1.add(message);
                    messages2.add(message);
                    chatHistory.put(senderid, messages1);
                    chatHistory.put(receiverid, messages2);
                }
            }
        else {
            throw new UserNotFoundException("The receiver does not exist or is not a permanent user." +
                    " You can not send message. ");
            }
        }

    /**
     * a method used to make a copy of a message
     * @param message the message we want to copy.
     * @return a message copy.
     */

    public Message makeMessageCopy(Message message){
        Message copy = new Message(message.getSenderID(), message.getReceiverID(),
                message.getContent());
        copy.setCreation(message.getCreation());
        return copy;
    }

    /**
     * a method used to delete a message.
     * @param message the message we want to delete
     * @param ownerid the operating user id
     */

    public void deleteMessage(Message message, String ownerid) {
        chatHistory.get(ownerid).remove(message);
    }

    /**
     * a method used to delete a message from the commonInbox
     * @param message the message we want to delete.
     */

    public void deleteCommonInbox(Message message){
        commonInbox.remove(message);
    }

    /**
     * a getter of the ChatHistory.
     * @return  the chat history.
     */

    public HashMap<String, ArrayList<Message>> getChatHistory() {
        return chatHistory;
    }

    /**
     * a getter of the commonInbox
     * @return the commonInbox.
     */
    public ArrayList<Message> getCommonInbox() {
        return commonInbox;
    }

    public void commonInboxToString(){
        System.out.println("The commonInbox contains messages below: ");
        for (Message msg: commonInbox){
            System.out.println(msg + "\n");
        }
    }

    /**
     * a toString method that shows a user's personal chat history.
     * @param userid the id of the user
     */

    public void UserChatHistoryString(String userid){
        ArrayList<Message> msgs = chatHistory.get(userid);
        System.out.println("\nUserid: " + userid + "\nPersonalChatHistory: " + msgs + "\n");
    }

    /**
     * a toString method that shows all users' chat history.
     */

    public void AllChatHistoryString(){
        Map<String, ArrayList<Message>> map = chatHistory;
        for (Map.Entry<String, ArrayList<Message>> entry : map.entrySet()) {
            String userid = entry.getKey();
            ArrayList<Message> msgs = entry.getValue();
            System.out.println("\nUserid: " + userid + "\nPersonalChatHistory: " + msgs + "\n");
       }
    }

    /**
     * a method to get the user's email according to its user id.
     * @param id the user id
     * @return the user's email.
     */
    public String getUserEmail(String id) {
        ArrayList<PermanentUser> pms = um.getPermanentUsers();
        for (PermanentUser user : pms) {
            if (user.getUserId().equals(id)) {
                return user.getEmail();
            }
        }
        return null;
    }
}

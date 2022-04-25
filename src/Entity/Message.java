package Entity;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * An class which is the message sent by users between each other.
 * It also has five attributes: SenderID, ReceiverID, Content, Time, and Creation
 *
 * Message can be created by the constructor immediately.
 *
 * SenderID: a unique id belonged to a permanent user who is the sender
 * ReceiverID: a unique id belonged to a permanent user who is the receiver
 * Content: a string that represents the content of this message
 * Time: time when this message is created, generated in the constructor
 * Creation: a schedule attached in this message, the default value is null
 *
 * @author Christine
 * @author Chuanrun Zhang
 * @author Siqing Xu
 */
public class Message implements Serializable {
    private String SenderID;
    private String ReceiverID;
    private String Content;
    private String Time;
    private Schedule Creation = null;

    /**
     * A constructor of Message object.
     * It creates a Message with id of its sender, id of its receiver, its content,
     * and time it created.
     * It created whenever a message is created.
     *
     * @param senderID the unique id belonged to a permanent user who is the sender
     * @param receiverID the unique id belonged to a permanent user who is the receiver
     * @param content the content of this message
     *
     */
    public Message (String senderID, String receiverID, String content){
        this.SenderID = senderID;
        this.ReceiverID = receiverID;
        this.Content = content;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.Time = dtf.format(now);
    }

    /**
     * Returns the id of the sender called SenderID.
     * It is a getter of SenderID of a Message.
     *
     * @return the id of the sender
     */
    public String getSenderID() {
        return SenderID;
    }

    /**
     * Returns the id of the receiver called ReceiverID.
     * It is a getter of ReceiverID of a Message.
     *
     * @return the id of the receiver
     */
    public String getReceiverID() {
        return ReceiverID;
    }

    /**
     * Returns the schedule attached to this message called Creation.
     * It is a getter of Creation of a Message.
     *
     * @return the schedule attached to this message
     */
    public Schedule getCreation() {
        return Creation;
    }

    /**
     * Returns the content of this message called Content.
     * It is a getter of Content of a Message.
     *
     * @return the content of this message
     */
    public String getContent() {
        return Content;
    }

    /**
     * Returns the time this message created called Time.
     * It is a getter of Time of a Message.
     *
     * @return the time this message created
     */
    public String getTime() {
        return Time;
    }

    /**
     * Assign the schedule attached to this message called Creation.
     * It is a setter of Creation of a Message.
     *
     * @param creation the schedule attached to this message
     */
    public void setCreation(Schedule creation) {
        Creation = creation;
    }

    /**
     * Assign the id of the sender of this message called SenderID.
     * It is a setter of SenderID of a Message.
     *
     * @param senderID the id of the sender of this message
     */
    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    /**
     * Assign the id of the receiver of this message called ReceiverID.
     * It is a setter of ReceiverID of a Message.
     *
     * @param receiverID the id of the receiver of this message
     */
    public void setReceiverID(String receiverID) {
        ReceiverID = receiverID;
    }

    /**
     * Assign the content of this message called Content.
     * It is a setter of Content of a Message.
     *
     * @param content the content of this message
     */
    public void setContent(String content) {
        Content = content;
    }

    /**
     * Returns the information of this message, including SenderID, ReceiverID, Content, Creation, and Time
     *
     * @return a string containing the information of this message
     */
    public String toString(){
        return "\nSenderID: " + this.SenderID + "\nReceiverID: " + this.ReceiverID + "\nMessage Content: " + this.Content
                + "\nRelated Schedule: " + this.Creation + "\nTime: " + this.Time;
    }
}


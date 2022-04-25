package Presenter;

import Controller.Facade;
import Entity.Message;
import UseCase.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * a controller class called MessageUI that is used to interact with users and their message by showing instructions
 * on console and getting user-input.
 *
 * facade: a Facade object
 * sc: a Scanner object to read input
 * userType: an attribute to store user's type
 * userId: an attribute to store user's id
 * sm: a ScheduleManager object
 * mm: a MessageManager object
 */

public class MessageUI {
    private Facade facade;
    private Scanner sc;
    private String userId;
    private ScheduleManager sm;
    private MessageManager mm;
    private ScheduleUI sui;

    /**
     * a constructor of MessageUI with parameters facade, scanner, userid and userType.
     * @param facade a Facade object
     * @param scanner a scanner object
     * @param userId the user id
     */

    public MessageUI(Facade facade, Scanner scanner, String userId) {
        this.facade = facade;
        sc = scanner;
        this.userId = userId;
        this.sm = this.facade.getSm();
        this.mm = this.facade.getMm();
        this.sui = new ScheduleUI(facade,sc);
    }

    /**
     * a method used to relates a schedule in a message so that the schedule is attached.
     * @param msg the message that the schedule wants to attach to
     * @throws ScheduleNotFoundException when a schedule is not found
     */
    private void attachSchedule(Message msg) throws ScheduleNotFoundException {
        while (true) {
            System.out.println("Do you want to attach a schedule in this message? Press '1' for yes, \n" +
                    "press '2' for no.");
            String option3 = sc.nextLine();
            if (option3.equals("1")) {
                System.out.println("Here are the schedules you can attach: ");
                String scheduleid = null;
                while (true) {
                    if (facade.ss.chooseMySchedule(userId)) break;
                    try{
                        int choice = Integer.parseInt(sc.nextLine());
                        scheduleid = facade.sm.getSchedulesList().get(userId).get(choice-1).getScheduleID();
                        break;
                    } catch (NumberFormatException e){
                        System.out.println("The input you entered is not an integer. Please try again.");
                    } catch (IndexOutOfBoundsException e){
                        System.out.println("There is no such schedule number."); } }
                try {
                    msg.setCreation(sm.getScheduleByID(scheduleid));
                    System.out.println("You have attach a schedule successfully.");
                } catch (ScheduleNotFoundException s){
                    System.out.println("The Schedule does not exist.");
                }break;
            } else if (option3.equals("2")) {
                break;
            } else {
                System.out.println("You have enter an invalid option. Please try again."); } } }

    /**
     * prints instructions on console for regular users when they wants to interact with message.
     * They can read their chat history, delete certain message, send message or reply certain message.
     * @throws ScheduleNotFoundException when a schedule is not found
     * @throws UserNotFoundException when a user is not found
     * @throws IOException when saveToFrw() and replyByReg() throws exception
     */
    public void runByRegular() throws ScheduleNotFoundException, UserNotFoundException, IOException {
        while (true) {
            System.out.println("enter '1' to see your personal chat history, \n" +
                    "enter '2' to delete message, \n" +
                    "enter '3' to send message,\n" +
                    "enter '4' to reply a message, \n" +
                    "enter '5' to link a schedule in a message, \n" +
                    "enter '6' to exit");
            String s = sc.nextLine();
            if (s.equals("1")) {
                mm.UserChatHistoryString(userId);
            } else if (s.equals("2")) {
                deleteMsgByReg(userId);
                saveToFrw();
                break;
            } else if (s.equals("3")) {
                sendByReg();
                break;
            } else if (s.equals("4")){
                replyByReg();
            } else if (s.equals("5")){
                linkSchedule(userId);
            } else if (s.equals("6")){
                saveToFrw();
                break;
            } else {
                System.out.println("You have enter an invalid option. Please try again.");
            }
        }
    }

    /**
     * a helper method to send messages by regular users. It allows regular to send messages to another regular user
     * or send to admin commonInbox.
     * @throws ScheduleNotFoundException when schedule is not found
     * @throws UserNotFoundException when a user is not found
     * @throws IOException when facade.ms.regularToAdmin(msg), facade.ms.regularToOther(msg) and
     * saveToFrw() throws exception
     */

    private void sendByReg() throws ScheduleNotFoundException, UserNotFoundException, IOException {
        while (true) {
            System.out.println("Who do you want to send to?\n" +
                    "Press '0' for admin, \n'1' for regular/public/private user");
            String option = sc.nextLine();
            if (option.equals("0")) {
                System.out.println("Please enter the message content below: ");
                String content = sc.nextLine();
                Message msg = facade.mm.createMessage(userId, "admin", content);
                attachSchedule(msg);
                facade.ms.sendMsg("regularToAdmin", msg);
                System.out.println("You have sent a message successfully.");
                saveToFrw();
                break;
            } else if (option.equals("1")) {
                System.out.println("Please enter the message content below: ");
                String content = sc.nextLine();
                String selfemail = facade.mm.getUserEmail(userId);
                System.out.println("Here are the email you can sent to: ");
                if(facade.ls.displayUserInfo(selfemail)){
                    System.out.println("There are no users except yourself in the App!");
                    break;
                } else{
                    System.out.println("Please enter the receiver email: ");
                }
                String receiveremail = sc.nextLine();
                String receiverid = facade.ls.getId(receiveremail);
                if (receiverid.equals("not found")){
                    System.out.println("The receiver does not exist.");
                } else if (facade.ls.getType(receiverid).equals("admin")){
                    System.out.println("The receiver is an admin user. Please send to admin common inbox instead.");
                }
                else{
                    Message msg = facade.mm.createMessage(userId, receiverid, content);
                    attachSchedule(msg);
                    facade.ms.sendMsg("regularToOther", msg);
                    System.out.println("You have sent a message successfully.");
                    saveToFrw();
                    break;
                }
            } else {
                System.out.println("You have enter an invalid option. Please try again.");
            }
        }
    }

    /**
     * prints instructions on console for admin users when they wants to interact with message.
     * They can read commonInbox, delete certain message, send message to a user, some users , all users, and reply
     * messages.
     * @throws ScheduleNotFoundException when schedule is not found
     * @throws UserNotFoundException when a user is not found
     * @throws IOException when facade.ms.adminToAll(msg), saveToFrw(), facade.ms.adminToOther(msg),
     * sendByAdmintoSome() throws exception
     */

    public void runByAdmin() throws ScheduleNotFoundException, UserNotFoundException, IOException {
        while (true) {
            System.out.println("Enter '0' to read messages in commonInbox, \n" +
                    "enter '1' to send message to all permanent users, \n" +
                    "enter '2' to send message to a particular user, \n" +
                    "enter '3' to send message to some users, \n" +
                    "enter '4' to delete messages in CommonInbox, \n" +
                    "enter '5' to reply a message, \n" +
                    "enter '6' to exit");
            String option = sc.nextLine();
            if (option.equals("0")){
                facade.mm.commonInboxToString(); break; }
            else if (option.equals("1")) {
                System.out.println("Please enter the message content below: ");
                String content = sc.nextLine();
                Message msg = facade.mm.createMessage("admin", "default", content);
                facade.ms.sendMsg("adminToAll", msg);
                System.out.println("You have sent a message successfully.");
                saveToFrw(); break;
            } else if (option.equals("2")){
                System.out.println("Please enter the message content below: ");
                String content = sc.nextLine();
                String selfemail = facade.mm.getUserEmail(userId);
                System.out.println("Here are the email you can sent to: ");
                if(facade.ls.displayUserInfo(selfemail)){
                    System.out.println("There are no users except yourself in the App!"); break;
                } else{
                System.out.println("Please enter the receiver email: "); }
                String receiveremail = sc.nextLine();
                String receiverid = facade.ls.getId(receiveremail);
                if(receiverid.equals("not found")){
                    System.out.println("The receiver does not exist.");
                } else if (facade.ls.getType(receiverid).equals("admin")){
                    System.out.println("The receiver is an admin user. Please send to admin common inbox instead.");
                }
                else{
                    Message msg = facade.mm.createMessage("admin",receiverid, content);
                    attachSchedule(msg);
                    facade.ms.sendMsg("adminToOther", msg);
                    System.out.println("You have sent a message successfully.");
                    saveToFrw();break; }
            } else if (option.equals("3")){
                sendByAdmintoSome();
            } else if (option.equals("4")){
                deleteMsgByAdmin();
            } else if (option.equals("5")){
                replyByAdmin();
            } else if (option.equals("6")){
                saveToFrw();
                break;
            } else{
                System.out.println("You have enter an invalid option. Please try again."); } } }

    /**
     * a helper method for admin users to reply a message in commonInbox. Admin can follow instructions to reply.
     */

    private void replyByAdmin(){
        System.out.println("The commonInbox contains messages below: ");
        HashMap<Integer, Message> indexToMsg = new HashMap<>();
        ArrayList<Message> msgs = facade.mm.getCommonInbox();
        if (msgs != null && !msgs.isEmpty()){
            int i = 1;
            for (Message msg : msgs){
                System.out.println(i + ": " + msg + '\n');
                indexToMsg.put(i, msg);
                i++;
            }
            System.out.println("Please enter the number in front to reply that message." );
            try{
                String num = sc.nextLine();
                int index = Integer.parseInt(num);
                if (indexToMsg.containsKey(index)){
                    Message original = indexToMsg.get(index);
                    Message copy = facade.mm.makeMessageCopy(indexToMsg.get(index));
                    copy.setSenderID(original.getReceiverID());
                    copy.setReceiverID(original.getSenderID());
                    if (copy.getReceiverID().equals(userId)||copy.getReceiverID().equals("admin")){
                        System.out.println("You cannot reply to yourself.");
                    } else {
                        System.out.println("Please enter the message content below: ");
                        String content = sc.nextLine();
                        copy.setContent(content);
                        facade.ms.sendMsg("adminToOther", copy);
                        saveToFrw();
                        System.out.println("You have successfully replied.");
                    }
                } else{
                    System.out.println("The number you have entered does not match. Please enter a valid one.");
                }
            } catch (NumberFormatException | IOException | UserNotFoundException e) {
                System.out.println("You need to input a number.");
            }
        } else{
            System.out.println("You don't have any messages right now. You cannot reply messages.");
        }
    }

    /**
     * a helper method used to reply message by regular user.
     * It prints the chat history the user has and allows him to select a message to reply.
     */
    private void replyByReg(){
        System.out.println("Your PersonalChatHistory contains messages below: ");
        HashMap<Integer, Message> indexToMsg = new HashMap<>();
        ArrayList<Message> msgs = facade.mm.getChatHistory().get(userId);
        if (msgs != null && !msgs.isEmpty()){
            int i = 1;
            for (Message msg : msgs){
                System.out.println(i + ": " + msg + '\n');
                indexToMsg.put(i, msg);
                i++;
            }
            System.out.println("Please enter the number in front to reply that message." );
            try{
                String num = sc.nextLine();
                int index = Integer.parseInt(num);
                if (indexToMsg.containsKey(index)){
                    Message original = indexToMsg.get(index);
                    Message copy = facade.mm.makeMessageCopy(indexToMsg.get(index));
                    copy.setSenderID(original.getReceiverID());
                    copy.setReceiverID(original.getSenderID());
                    if (copy.getReceiverID().equals(userId)){
                        System.out.println("You cannot reply to yourself.");
                    } else{
                        System.out.println("Please enter the message content below: ");
                        String content = sc.nextLine();
                        copy.setContent(content);
                        if (copy.getReceiverID().equals("admin")){
                            facade.ms.sendMsg("regularToAdmin", copy);
                        } else{
                            facade.ms.sendMsg("regularToOther", copy); }
                        saveToFrw();
                        System.out.println("You have successfully replied.");
                    }
                } else{
                    System.out.println("The number you have entered does not match. Please enter a valid one.");
                }
            } catch (NumberFormatException | IOException | UserNotFoundException e) {
                System.out.println("You need to input a number.");
            }
        } else{
            System.out.println("You don't have any messages right now. You cannot reply messages.");
        }
    }

    /**
     * a helper method used to send message from admin to some users.
     * @throws ScheduleNotFoundException when schedule is not found
     * @throws UserNotFoundException when a user is not found
     * @throws IOException when saveToFrw() throws exception
     */

    private void sendByAdmintoSome() throws UserNotFoundException, IOException {
        while (true) {
            System.out.println("Please enter the message content below: ");
            String content = sc.nextLine();
            String selfemail = facade.mm.getUserEmail(userId);
            System.out.println("Here are the email you can sent to: ");
            if (facade.ls.displayUserInfo(selfemail)) {
                System.out.println("There are no users except yourself in the App!");
                break;
            } else {
                System.out.println("Please enter the receiver emails you want to send, separated by ',' \n" +
                        "( Example: email1,email2,email3 ) ");
            }
            String receiveremails = sc.nextLine();
            String[] emails = receiveremails.split(",");
            for (String email : emails) {
                String id = facade.ls.getId(email);
                if (id.equals("not found")) {
                    System.out.println("The receiver does not exist.");
                } else if (facade.ls.getType(id).equals("admin")){
                    System.out.println("The receiver is an admin user. Please send to admin common inbox instead.");
                }
                else {
                    Message msg = facade.mm.createMessage("admin", id, content);
                    facade.ms.sendMsg("adminToOther", msg);
                    System.out.println("You have sent a message successfully.");
                }
            }
            saveToFrw();
            break;
        }
    }

    /**
     * a helper method that is used to delete a message in the commonInbox.
     * It prints instructions that allows admin user to delete the message according to the corresponding front number.
     */

    private void deleteMsgByAdmin(){
        System.out.println("The CommonInbox contains messages below: ");
        HashMap<Integer, Message> indexToMsg = new HashMap<>();
        if (facade.mm.getCommonInbox() != null && !facade.mm.getCommonInbox().isEmpty()){
            int i = 1;
            for (Message msg : facade.mm.getCommonInbox()){
                indexToMsg.put(i, msg);
                System.out.println(i + ": " + msg + '\n');
                i++;
            }
            System.out.println("Please enter the number in front to delete that message." );
            try{
                String num = sc.nextLine();
                int index = Integer.parseInt(num);
                if (indexToMsg.containsKey(index)){
                    facade.mm.deleteCommonInbox(indexToMsg.get(index));
                    saveToFrw();
                    System.out.println("You have successfully deleted the message in Common Inbox.");
                }
                else{
                    System.out.println("The number you have entered does not match. Please enter a valid one.");
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println("You need to input a number.");
            }
        }
        else{
            System.out.println("The Common Inbox does not contain any messages. You cannot delete message right now.");
        }
    }

    /**
     * a helper method to interact a schedule in the personal chat history existed messages. User can edit schedules
     * in a message which contains a schedule.
     * @param userId the user's id
     */
    private void linkSchedule(String userId) {
        while (true) {
            System.out.println("Your PersonalChatHistory contains messages below: ");
            HashMap<Integer, Message> indexToMsg = new HashMap<>();
            ArrayList<Message> msgs = facade.mm.getChatHistory().get(userId);
            if (msgs != null && !msgs.isEmpty()) {
                int i = 1;
                for (Message msg : msgs) {
                    indexToMsg.put(i, msg);
                    System.out.println(i + ": " + msg + '\n');
                    i++;
                }
                System.out.println("Please enter the number in front to link the related schedule.");
                try {
                    String num = sc.nextLine();
                    int index = Integer.parseInt(num);
                    if (indexToMsg.containsKey(index)) {
                        Message message = indexToMsg.get(index);
                        if (message.getCreation() == null) {
                            System.out.println("There is no related schedule in this message.");
                            break;
                        }
                        sui.editSchedule(message.getCreation().getScheduleID());
                        facade.saveSchedule(message.getCreation().getScheduleID());
                        System.out.println("You have successfully edit the schedule.");
                        break;
                    } else {
                        System.out.println("The number you have entered does not match. Please enter a valid one.");
                    }
                } catch (NumberFormatException | IOException e) {
                    System.out.println("You need to input a number.");
                }
            } else {
                System.out.println("You don't have any messages right now. You cannot link schedule.");
                break;
            }
        }
    }

    /**
     * a helper method that used to delete a message in the chat history by a regular user.
     * It prints instructions that allows regular user to delete the message according to the corresponding front number.
     * @param userId the id of the user
     */
    private void deleteMsgByReg(String userId){
        System.out.println("Your PersonalChatHistory contains messages below: ");
        HashMap<Integer, Message> indexToMsg = new HashMap<>();
        ArrayList<Message> msgs = facade.mm.getChatHistory().get(userId);
        if (msgs != null && !msgs.isEmpty()){
            int i = 1;
            for (Message msg : msgs){
            indexToMsg.put(i, msg);
            System.out.println(i + ": " + msg + '\n');
            i++; }
            System.out.println("Please enter the number in front to delete that message." );
            try{
                String num = sc.nextLine();
                int index = Integer.parseInt(num);
                if (indexToMsg.containsKey(index)){
                    facade.mm.deleteMessage(indexToMsg.get(index), userId);
                    facade.mm.UserChatHistoryString(userId);
                    saveToFrw();
                    System.out.println("You have successfully deleted the message in your personal chat history.");
                } else{ System.out.println("The number you have entered does not match. Please enter a valid one."); }
            } catch (NumberFormatException | IOException e) {
                System.out.println("You need to input a number."); }
        } else{
            System.out.println("You don't have any messages right now. You cannot delete messages."); } }

    /**
     * a method that saves chatHistory and commonInbox information into files when new message is stored or deleted.
     * @throws IOException when facade.frw.saveFile() and facade.frw.events.notify() throws exception.
     */

    public void saveToFrw() throws IOException {
        facade.frw.saveFile("saveCommonInbox");
        facade.frw.saveFile("saveChatHistory");
        facade.frw.events.notify("saveCommonInbox");
        facade.frw.events.notify("saveChatHistory");
    }

}


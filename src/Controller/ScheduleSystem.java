package Controller;

import Entity.Schedule;
import UseCase.*;

import java.text.ParseException;
import java.util.*;

/**
 * A class called ScheduleSystem which is a controller of ScheduleManager.
 * ScheduleSystem can be created by the constructor immediately with the given attributes.
 * tm: a TemplateManager
 * sm: a ScheduleManager
 * previousStatus: store previous status of a schedule.
 * deletedSchedule: store all deleted schedule in a list, last element is most recently deleted schedule.
 * deletedTempID: store all deleted template id in a list.
 * deletedEventName: map a schedule id to a list of event name that has been deleted from this schedule before.
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 */
public class ScheduleSystem {
    private ScheduleManager sm;
    private TemplateManager tm;
    private HashMap<String, List<String>> deletedEventName = new HashMap<>();
    private ArrayList<String> previousStatus = new ArrayList<>();
    private ArrayList<Schedule> deletedSchedule = new ArrayList<>();
    private ArrayList<String> deletedTempID = new ArrayList<>();

    /**
     * A constructor that create a ScheduleSystem.
     * @param tm a TemplateManager
     * @param sm a ScheduleManager
     */
    public ScheduleSystem(ScheduleManager sm, TemplateManager tm) {
        this.sm = sm;
        this.tm = tm;
    }

    /**
     * Creates a Schedule with the given attributes. Return schedule's id.
     * @param userID       the id of user
     * @param status       public or private
     * @param scheduleName the name of the schedule
     * @param scheduleDate the date of schedule
     * @param templateID   the id of the template
     * @return schedule's id
     * @throws ParseException when scheduleDate is in incorrect format.
     */
    public String createSchedule(String userID, String status, String scheduleName, String scheduleDate, String templateID) throws ParseException {
        String returned = null;
        try {
            returned = sm.createSchedule(status, scheduleName, scheduleDate, userID, tm, templateID);
        } catch (TemplateNotFoundException e) {
            System.out.println("Error: Template invalid!");
        }
        return returned;
    }

    /**
     * Delete a schedule according to its schedule ID.
     * @param scheduleID schedule's id
     * @return true if the schedule is deleted successfully.
     * @throws ScheduleNotFoundException if the schedule is not found according to the schedule id.
     */
    public boolean deleteSchedule(String scheduleID) throws ScheduleNotFoundException {
        if(sm.deleteSchedule(scheduleID)){
            return true;
        }else{
            throw new ScheduleNotFoundException("Schedule is not found");
        }
    }

    /**
     * Add s and tempID to deletedSchedule and deletedTempID.
     * @param s deleted schedule
     * @param tempID deleted template id.
     */
    public void addDeletedScheduleAndTemp(Schedule s, String tempID) {
        deletedSchedule.add(s);
        deletedTempID.add(tempID);
    }

    /**
     * Recover the most recently deleted schedule.
     */
    public void recoverSchedule() {
        if(deletedSchedule.isEmpty()){
            System.out.println("There is no previously deleted schedule.");
        }
        else {
            Schedule recoveredS = deletedSchedule.get(deletedSchedule.size() - 1);
            String newTempID = deletedTempID.get(deletedTempID.size() - 1);
            sm.getSchedulesList().get(recoveredS.getAuthor()).add(recoveredS);
            sm.getScheduleTempMap().put(recoveredS.getScheduleID(), newTempID);
            System.out.println(recoveredS);
            System.out.println("This is the recovered schedule.");
            deletedSchedule.remove(deletedSchedule.size() -1);
        }
    }

    /**
     * Clear all deleted.
     */
    public void clearDeletedSchedulesAndEvents() {
        deletedSchedule = new ArrayList<>();
        deletedTempID = new ArrayList<>();
        deletedEventName = new HashMap<>();
        sm.clearDeletedEvents();
    }

    /**
     * Add schedule's current status to previousStatus to store it.
     * @throws ScheduleNotFoundException when the given id is not valid.
     */
    public void addPreviousStatus(String prevstatus) throws ScheduleNotFoundException {
        previousStatus.add(prevstatus);
    }

    /**
     * Restore schedule's most recently deleted status.
     * @param scheduleId schedule id
     * @param um UserManager
     * @throws SameStatusException when the most recent deleted status is same as current status, which is not possible.
     */
    public void restoreStatus(String scheduleId, UserManager um) throws SameStatusException {
        if(previousStatus.isEmpty()){
            System.out.println("There is no previous status of this schedule.");
        }
        else {
            sm.changeStatus(scheduleId, previousStatus.get(previousStatus.size() - 1));
            System.out.println("This schedule's status is changed to: " +
                    previousStatus.get(previousStatus.size() - 1));
            if (previousStatus.get(previousStatus.size() -1).equals("friend-only")){
                try {
                    sm.shareScheduleToFriends(scheduleId, um);
                } catch (ScheduleNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            previousStatus.remove(previousStatus.size() - 1);
        }
    }

    /**
     * Clear all recently deleted status
     */
    public void clearPreviousStatus() {
        previousStatus = new ArrayList<>();
    }

    /**
     * add a event name to deletedEventName
     * @param eventName event name that is going to be added
     */
    public void addDeletedEvent(String scheduleID, String eventName) {
        if (deletedEventName.containsKey(scheduleID)) {
            deletedEventName.get(scheduleID).add(eventName);
        } else {
            List<String> deletedEvents = new ArrayList<>();
            deletedEvents.add(eventName);
            deletedEventName.put(scheduleID, deletedEvents);
        }

    }

    /**
     * recover the most recently deleted event, print out the event info
     * @param scheduleId schedule id that the event originally belongs to
     * @throws ScheduleNotFoundException the given scheduleID is not valid
     * @throws UserNotFoundException userid is not valid.
     */
    public void recoverEvent(String scheduleId) throws ScheduleNotFoundException, UserNotFoundException {
        if(!(deletedEventName.containsKey(scheduleId)) || deletedEventName.get(scheduleId).isEmpty()){
            System.out.println("There is no previously deleted event.");
        }
        else {
            List<String> deletedEvents = deletedEventName.get(scheduleId);
            String recoverEvent = deletedEvents.get(deletedEvents.size() - 1);
            HashMap<String, List<Date>> deletedList = sm.getDeletedEvents(scheduleId);
            List<Date> dates = deletedList.get(recoverEvent);
            sm.addValidEvent(dates, sm.getScheduleByID(scheduleId), recoverEvent);
            sm.removeDeletedEvents(scheduleId, recoverEvent);
            deletedEventName.get(scheduleId).remove(recoverEvent);
            System.out.println("This is the recovered event:");
            System.out.println("Event name:" + recoverEvent);
            System.out.println("Event time:" + dates);
        }
    }

    /**
     * Display public schedule list.
     */
    public void displayPublicSchedule(){
        if (sm.getAllPublicSchedule().isEmpty()) {
            System.out.println("There is no public schedule.");
        }
        for (Schedule s : sm.getAllPublicSchedule()) {
            System.out.println(s);
        }
    }

    /**
     * Display schedules the particular user created.
     * @param userID user id
     * @throws ScheduleNotFoundException if the schedule is not found according to the schedule id.
     */
    public void displayUserSchedule(String userID) throws ScheduleNotFoundException {
        int i = 1;
        for (Schedule s : sm.getAllUserSchedule(userID)) {
                System.out.println(i + ") " + s);
                i++;
        }
    }

    /**
     * Display all friends' friend-only schedules.
     * @param userID user id.
     */
    public void displayFriendsSchedule(String userID){
        int i = 1;
        for (Schedule s : sm.getAllFriendsSchedule(userID)) {
            System.out.println(i + ") " + s);
            i++;
        }
    }

    /**
     * Display all events of a schedule.
     * @param scheduleID schedule's id
     * @throws ScheduleNotFoundException id is not valid.
     * @throws UserNotFoundException id is not valid.
     */
    public void displayScheduleEvents(String scheduleID) throws ScheduleNotFoundException{
        if (sm.getScheduleEvents(scheduleID).isEmpty()) {
            System.out.println("There is no event in this schedule.");
        } else {
            System.out.println(sm.getScheduleEvents(scheduleID));
        }
    }

    /**
     * Check if there is no schedule store in ScheduleManager.
     * @return true if there is no schedule.
     */
    public boolean checkNoSchedule(){
        boolean noSchedule = true;
        HashMap<String, List<Schedule>> allSchedules = sm.getSchedulesList();
        for (List<Schedule> slist : allSchedules.values()) {
            if (!(slist.isEmpty())) {
                noSchedule = false;
                break;
            }
        }
        return noSchedule;
    }

    /**
     * Return all schedules by all users.
     * @return a hashmap that its key is index start from 1, values are schedules.
     * @throws ScheduleNotFoundException when there is no schedule.
     */
    public HashMap<Integer, String> GetScheduleRecords() throws ScheduleNotFoundException {
        if (checkNoSchedule()) {
            throw new ScheduleNotFoundException("There is no schedule.");
        }
        HashMap<Integer, String> indexToId = new HashMap<>();
        HashMap<String, List<Schedule>> allSchedules = sm.getSchedulesList();
        int i = 1;
        for (List<Schedule> slist: allSchedules.values()) {
            for (Schedule s: slist){
                System.out.println(i + ") " + s);
                indexToId.put(i, s.getScheduleID());
                i++;
            }
        }
        return indexToId;
    }

    /**
     * When a new friend is added, update friend-only schedules list.
     * @param userID owner's user id
     * @param friID friend's user id.
     */
    public void updateFriends(String userID, String friID){
        updateOneFriend(userID, friID);
        updateOneFriend(friID, userID);
    }

    /**
     * Share user's friend-only schedules to friend
     * @param userID user's id
     * @param friID friend's id
     */
    private void updateOneFriend(String userID, String friID) {
        if (!(sm.getSchedulesList().containsKey(userID))){
            return;
        }
        for (Schedule s: sm.getSchedulesList().get(userID)){
            if(s.getStatus().equals("friend-only")){
                if(sm.getSchedulesFriend().containsKey(friID)){
                    sm.getSchedulesFriend().get(friID).add(s);
                }
                else{
                    ArrayList<Schedule> sList = new ArrayList<>();
                    sList.add(s);
                    sm.getSchedulesFriend().put(friID, sList);
                }
            }
        }
    }

    /**
     * Check if user's input for status is valid, if not valid, continue prompting.
     * @param userType user type
     * @param sc scanner
     * @return final valid status
     */
    public String checkStatusInputValid(String userType, Scanner sc) {
        String status;
        while (true) {
            if (userType.equals("admin")){
                System.out.println("Please enter schedule status: public or private");
                status = sc.nextLine();
                if (!(status.equals("public") || status.equals("private"))) {
                    System.out.println("Input invalid.");
                } else { break;}
            } else {
                System.out.println("Please enter schedule status: public, private or friend-only");
                status = sc.nextLine();
                if (!(status.equals("public") || status.equals("private") || status.equals("friend-only"))) {
                    System.out.println("Input invalid.");
                } else { break; }
            }
        }
        return status;
    }

    /**
     * Let user enter schedule date based on template type.
     * @param templateId template id.
     * @throws TemplateNotFoundException given id is not valid.
     */
    public void enterDatebyTemplateType(String templateId) throws TemplateNotFoundException {
        if (tm.getTemplateById(templateId).getTemplateType().equals("DailyTemplate")) {
            System.out.println("Please enter schedule date: yyyy MM dd");
        } else if (tm.getTemplateById(templateId).getTemplateType().equals("MonthlyTemplate")) {
            System.out.println("Please enter schedule month: yyyy MM");
        } else{
            System.out.println("Please enter schedule date: yyyy MM dd. This is going to be the first day of" +
                    " your weekly schedule.");
        }
    }

    /**
     * return list of start and end time of a event.
     * @param scheduleId schedule's id
     * @param sc scanner
     * @return Arraylist. first element is start time, second element is end time.
     * @throws ScheduleNotFoundException id invalid.
     */
    public ArrayList<String> getStartEndbyType(String scheduleId, Scanner sc) throws ScheduleNotFoundException {
        ArrayList<String> startEnd = new ArrayList<>();
        if (sm.getScheduleByID(scheduleId).getType().equals("Daily")) {
            System.out.println("Please input the start time of this new event: hh:mm \n" +
                    "(Please use 24 hour format.)");
            startEnd.add(sc.nextLine());
            System.out.println("Please input the end time of this new event: hh:mm \n" +
                    "(Please use 24 hour format.)");
        } else if (sm.getScheduleByID(scheduleId).getType().equals("Monthly")) {
            System.out.println("Please input the start time of this new event: dd hh:mm \n" +
                    "(Please use 24 hour format.)");
            startEnd.add(sc.nextLine());
            System.out.println("Please input the end time of this new event: dd hh:mm \n" +
                    "(Please use 24 hour format.)");
        } else{
            System.out.println("Please input the start time of this new event: MM dd hh:mm \n" +
                    "(Please use 24 hour format.)");
            startEnd.add(sc.nextLine());
            System.out.println("Please input the end time of this new event: MM dd hh:mm \n" +
                    "(Please use 24 hour format.)");
        }
        startEnd.add(sc.nextLine());
        return startEnd;
    }

    /**
     * Display user's schedules and let user choose one.
     * @param userId current user's id.
     * @return true if no schedule, false otherwise.
     */
    public boolean chooseMySchedule(String userId) {
        try {
            displayUserSchedule(userId);
        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
            return true;
        }
        System.out.println("The above are the schedules you have.");
        System.out.println("Please input the number in front of the schedule you want to see or edit: ");
        return false;
    }

    /**
     * Let user create a new event
     * @param scheduleId schedule id
     * @param sc scanner
     */
    public void generateNewEvent(String scheduleId, Scanner sc){
        while (true) {
            System.out.println("Please input the name of a new event you want to create,\n" +
                    "or input the name of the event you have already created, \n" +
                    "or enter '-1' to exit");
            String edit_input = sc.nextLine();
            if (edit_input.equals("-1")) {
                break;
            }
            try {
                ArrayList<String> startEnd = getStartEndbyType(scheduleId, sc);
                sm.addEvent(scheduleId, edit_input, startEnd.get(0), startEnd.get(1), tm);
                System.out.println("You have added new time to this event successfully.");
                break;
            } catch (ParseException | TemplateNotFoundException | BetweenException | DurationException |
                    StartEndException | ScheduleNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

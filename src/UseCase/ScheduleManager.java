package UseCase;

import Entity.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class which is used to edit schedule and edit events in schedule.
 * Can be created by the constructor immediately with the given HashMap schedulesList and scheduleTempMap.
 * schedulesList: HashMap that map userID to the list of schedules they created.
 * scheduleTempMap: HashMap that map ScheduleID to template name.
 * schedulesFriend: HashMap that map userID to the list of schedules their friends shared with them.
 * factory: used to generate different types of schedules upon request
 */
public class ScheduleManager {
    private HashMap<String, List<Schedule>> schedulesList;
    private HashMap<String, String> scheduleTempMap;
    private HashMap<String, List<Schedule>> schedulesFriend;
    private ScheduleFactory factory;

    /**
     * constructs ScheduleManager with schedulesList, scheduleTempMap, schedulesFriend and factory.
     */
    public ScheduleManager(){
        schedulesList = new HashMap<>();
        scheduleTempMap = new HashMap<>();
        schedulesFriend = new HashMap<>();
        this.factory = new ScheduleFactory();
    }

    /**
     * Setter for schedulesList
     * @param schedulesList new one to set
     */
    public void setSchedulesList(HashMap<String, List<Schedule>> schedulesList) {
        this.schedulesList = schedulesList;
    }

    /**
     * Setter for sceduleTempMap
     * @param scheduleTempMap new one to set
     */
    public void setScheduleTempMap(HashMap<String, String> scheduleTempMap) {
        this.scheduleTempMap = scheduleTempMap;
    }

    /**
     * Setter for schedulesFriend
     * @param schedulesFriend new one to set
     */
    public void setSchedulesFriend(HashMap<String, List<Schedule>> schedulesFriend) {
        this.schedulesFriend = schedulesFriend;
    }

    /**
     * Getter for schedulesFriend
     * @return current one
     */
    public HashMap<String, List<Schedule>> getSchedulesFriend() {
        return schedulesFriend;
    }

    /**
     * Gets schedulesList.
     * Getter of schedulesList.
     * @return HashMap that map userID to the list of schedules they created.
     */
    public HashMap<String, List<Schedule>> getSchedulesList() {
        return schedulesList;
    }

    /**
     * Getter of scheduleTempMap.
     * @return HashMap that map ScheduleID to template id.
     */
    public HashMap<String, String> getScheduleTempMap() {
        return scheduleTempMap;
    }

    /**
     * delete a schedule with given scheduleID and userID, return true if delete successfully, false otherwise.
     * @param scheduleID schedule's id.
     * @return return true if delete successfully, false otherwise.
     */
    public boolean deleteSchedule(String scheduleID){
        for (List<Schedule> slist : schedulesList.values()){
            for (Schedule s : slist) {
                if (s.getScheduleID().equals(scheduleID)) {
                    slist.remove(s);
                    scheduleTempMap.remove(s.getScheduleID());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Given scheduleID, userID, change the matched schedule to new status.
     * @param scheduleID schedule's id.
     * @param status either 'public', 'private', 'friend-only'. If it is something other than "public", "private" or "friend-only" then nothing will happen
     */
    public void changeStatus(String scheduleID, String status) throws SameStatusException {
        try {
            changeStatusHelper(getScheduleByID(scheduleID), status);
            getScheduleByID(scheduleID).setStatus(status);
        } catch (ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * helper of changeStatus
     * @param s schedule
     * @param status new status
     * @throws SameStatusException when new and current statuses are same
     */
    public void changeStatusHelper(Schedule s, String status) throws SameStatusException {
        if (s.getStatus().equals(status)) {
            throw new SameStatusException("The schedule is already in the status you want to change to.");
        }
        if (s.getStatus().equals("friend-only")) {
            for (List<Schedule> slist1 : schedulesFriend.values()) {
                slist1.remove(s);
            }
        }
    }

    /**
     * add event to a given schedule, throw exception when fails.
     * @param scheduleID schedule's id.
     * @param eventName name of event that is going to be added.
     * @param startTimeString start time of event, in format of (dd HH:mm) for MonthlySchedule, (HH:mm) for DailySchedule, (MM dd HH:mm) for Weekly Schedule.
     * @param endTimeString end time of event, in format of (dd HH:mm) for MonthlySchedule, (HH:mm) for DailySchedule, (MM dd HH:mm) for Weekly Schedule.
     * @param t TemplateManager
     * @throws ParseException when start time and end time not in correct format
     * @throws TemplateNotFoundException which is threw from helper validEvent.
     * @throws BetweenException when Between time is too short.
     * @throws DurationException when duration is too short or too long.
     * @throws StartEndException when the start time is after end time.
     * @throws ScheduleNotFoundException when the given scheduleID is not valid.
     */
    public void addEvent(String scheduleID, String eventName, String startTimeString, String endTimeString, TemplateManager t)
            throws ParseException, TemplateNotFoundException, BetweenException, DurationException, StartEndException, ScheduleNotFoundException {
        Schedule s = getScheduleByID(scheduleID);
        List<Date> DateList = new ArrayList<>();
        if (!(s.getType().equals("Weekly"))){
            if (s.checkDateFormat(startTimeString, endTimeString)){
            DateList.add(isDateValid(getScheduleByID(scheduleID).getScheduleDate() + " " + startTimeString, "yyyy MM dd HH:mm"));
            DateList.add(isDateValid(getScheduleByID(scheduleID).getScheduleDate() + " " + endTimeString, "yyyy MM dd HH:mm"));
        } }else{
            if (s.checkDateFormat(startTimeString, endTimeString)){
                String[] splited = s.getScheduleDate().split(" ");
                String year = splited[0];
                DateList.add(isDateValid(year+ " " + startTimeString, "yyyy MM dd HH:mm"));
                DateList.add(isDateValid(year+ " " + endTimeString, "yyyy MM dd HH:mm"));
            }else {
                throw new StartEndException("The start time or the end time is not within the week of this schedule!");
            }
        }
        if (validEvent(s, t, DateList.get(0), DateList.get(1))){
            addValidEvent(DateList, s, eventName);
        }
    }

    /**
     * add a valid event to a given schedule.
     * @param DateList a list that contain valid start and end time of the event.
     * @param s Schedule that the new event should be added on.
     * @param eventName event's name of the new event.
     */
    public void addValidEvent(List<Date> DateList, Schedule s, String eventName){
        HashMap<String, List<List<Date>>> events = s.getEvents();
        if (events.containsKey(eventName)){
            events.get(eventName).add(DateList);
        }
        else{
            List<List<Date>> StartEnd = new ArrayList<>();
            StartEnd.add(DateList);
            events.put(eventName, StartEnd);
        }
        s.setEvents(events);
    }

    /**
     * check whether the the given DateList is valid for a event that can be added to the given schedule.
     * @param s Schedule.
     * @param t TemplateManager.
     * @param start start time in Date.
     * @param end end time in Date.
     * @return true if the DateList is valid, but never returns false.
     * @throws TemplateNotFoundException when there is no matching template with the schedule.
     * @throws StartEndException when the start time is after end time.
     * @throws DurationException when duration is too short or too long.
     * @throws BetweenException when Between time is too short.
     */
    public boolean validEvent(Schedule s, TemplateManager t, Date start, Date end) throws TemplateNotFoundException,
            StartEndException, DurationException, BetweenException {
        Template temp = t.getTemplateById(scheduleTempMap.get(s.getScheduleID()));
        if (start.after(end)) {
            throw new StartEndException("Error: The start time is after the end time.");
        }
        validDuration(start, end, temp.getMinTimeOfEvent(), temp.getMaxTimeOfEvent());
        if (temp.getMinTimeBtwEvents() == -1) {
            return true;
        }
        validBetweenTime(s, start, end, temp);
        return true;
    }

    /**
     * check if start and end has valid between time based on template given.
     * @param s Schedule, event of start and end is going to be added to.
     * @param start start time of event.
     * @param end end time of event.
     * @param temp Template that with limitation of the event.
     * @throws BetweenException when Between time is too short.
     */
    private void validBetweenTime(Schedule s, Date start, Date end, Template temp) throws BetweenException {
        Collection<List<List<Date>>> all_time = s.getEvents().values();
        for (List<List<Date>> event_time_list : all_time) {
            for (List<Date> event_time : event_time_list) {
                if (event_time.get(0).after(start) && temp.getMinTimeBtwEvents() > event_time.get(0).getTime() - end.getTime()) {
                    throw new BetweenException("Error: Between time is too short, move event forward.");
                }
                if (start.after(event_time.get(0)) && temp.getMinTimeBtwEvents() > start.getTime() - event_time.get(1).getTime()) {
                    throw new BetweenException("Error: Between time is too short, move event afterward.");
                }
            }
        }
    }

    /**
     * check if min <= duration <= max.
     * @param start start time of event.
     * @param end end time of event.
     * @param min minimum duration of events.
     * @param max maximum duration of events.
     * @throws DurationException when duration is too short or too long.
     */
    private void validDuration(Date start, Date end, double min, double max) throws DurationException {
        double diff = (end.getTime() - start.getTime());
        double denominator = (60 * 60 * 1000);
        double duration = diff / denominator;
        if (duration < min) {
            throw new DurationException("Error: Duration is too short.");
        } else if (duration > max) {
            throw new DurationException("Error: Duration is too long.");
        }
    }

    /**
     * delete event from given schedule, return true if delete successfully.
     * @param scheduleID schedule's id.
     * @param EventName name of event that is going to be deleted.
     * @param startTimeString start time of event that is going to be deleted, in format (HH:mm) for DailySchedule, (dd HH:mm) for MonthlySchedule.
     * @param endTimeString end time of event that is going to be deleted.
     * @return true if the event is successfully deleted.
     * @throws ParseException when startTimeString or endTimeString is not in correct format.
     * @throws ScheduleNotFoundException when scheduleID does not exist.
     */
    public boolean deleteEvent (String scheduleID, String EventName, String startTimeString, String endTimeString) throws ParseException, ScheduleNotFoundException {
        Schedule s = getScheduleByID(scheduleID);
        HashMap<String, List<List<Date>>> events = getScheduleByID(scheduleID).getEvents();
        if (!(events.containsKey(EventName))){
            return false;
        }
        s.checkDateFormat(startTimeString, endTimeString);
        Date dateST;
        Date dateET;
        if (s.getType().equals("Weekly")){
            String[] splited = s.getScheduleDate().split(" ");
            String year = splited[0];
            dateST = new SimpleDateFormat("yyyy MM dd HH:mm").parse(year + " " + startTimeString);
            dateET = new SimpleDateFormat("yyyy MM dd HH:mm").parse(year + " " + endTimeString);
        } else {
            dateST = new SimpleDateFormat("yyyy MM dd HH:mm").parse(s.getScheduleDate() + " " + startTimeString);
            dateET = new SimpleDateFormat("yyyy MM dd HH:mm").parse(s.getScheduleDate() + " " + endTimeString);
        }
        return deleteValidEvent(events.get(EventName), dateST, dateET, EventName, events, s);
    }

    /**
     * delete event with valid start and end time. Return true if delete successfully, false otherwise.
     * @param listOfDates lists of start and end time that has EventName.
     * @param dateST start time of event.
     * @param dateET end time of event.
     * @param EventName event name.
     * @param events all events that schedule s currently have.
     * @param s schedule that the event should be deleted from.
     * @return true if deleted successfully. False otherwise.
     */
    private boolean deleteValidEvent(List<List<Date>> listOfDates, Date dateST, Date dateET, String EventName,
                                     HashMap<String, List<List<Date>>> events, Schedule s) throws ScheduleNotFoundException {
        for (List<Date> time: listOfDates){
            if (time.get(0).equals(dateST) && time.get(1).equals(dateET)){
                addDeletedEvents(s.getScheduleID(), EventName, time);
                listOfDates.remove(time);
                if (listOfDates.isEmpty()){
                    events.remove(EventName);
                }
                s.setEvents(events);
                return true;
            }
        }
        return false;
    }

    /**
     * create new schedule with given name, date, author, template
     * @param status schedule's status, either public, private or friend-only.
     * @param scheduleName schedule's name.
     * @param scheduleDate schedule's date, in format of (yyyy-MM-dd) for DailySchedule and WeeklySchedule, (yyyy-MM) for MonthlySchedule.
     * @param author user's id.
     * @param t TemplateManager.
     * @param templateId id of template that is used as basis to create this schedule.
     * @return created schedule's id.
     * @throws TemplateNotFoundException when the given id does not exist.
     * @throws ParseException when scheduleDate is in incorrect format.
     */
    public String createSchedule(String status, String scheduleName, String scheduleDate, String author, TemplateManager t,
                               String templateId) throws TemplateNotFoundException, ParseException {
        Template temp = t.getTemplateById(templateId);
        String type = temp.getTemplateType();
        Schedule s = factory.getSchedule(type, scheduleDate, scheduleName, author, status);
        if (schedulesList.containsKey(author)){
            schedulesList.get(author).add(s);
        }
        else{
            List<Schedule> newList = new ArrayList<>();
            newList.add(s);
            schedulesList.put(author,newList);
        }
        scheduleTempMap.put(s.getScheduleID(), templateId);
        return s.getScheduleID();
    }

    /**
     * Return all schedules that are created by the given user id.
     * @param userID user's id.
     * @return list of schedules.
     * @throws ScheduleNotFoundException when schedulesList is empty.
     */
    public List<Schedule> getAllUserSchedule(String userID) throws ScheduleNotFoundException {
        List<Schedule> user_schedule = new ArrayList<>();
        if (!(schedulesList.containsKey(userID))|| schedulesList.get(userID).isEmpty()) {
            throw new ScheduleNotFoundException("You don't have any schedules.");
        }
        for (Schedule s: schedulesList.get(userID)) {
            user_schedule.add(s);
        }
        return user_schedule;
    }

    /**
     * Return a list that contain all schedules that are shared by friends
     * @param userID user's id
     * @return a list that contain all schedules that are shared by friends
     */
    public List<Schedule> getAllFriendsSchedule(String userID){
        List<Schedule> friend_schedule = new ArrayList<>();
        if (!(schedulesFriend.containsKey(userID)) || schedulesFriend.get(userID).isEmpty()){
            System.out.println("You don't have any friends-shared schedules.");
        }
        if (schedulesFriend.containsKey(userID)){
            friend_schedule.addAll(schedulesFriend.get(userID));
        }
        return friend_schedule;
    }

    /**
     * Display the schedule events by the given schedule id.
     * @param scheduleID schedule's id.
     * @return schedule's events.
     * @throws ScheduleNotFoundException There is no schedule corresponding to this schedule id.
     */
    public HashMap<String, List<List<Date>>> getScheduleEvents(String scheduleID) throws ScheduleNotFoundException {
        HashMap<String, List<List<Date>>> schedule_events;
        schedule_events = getScheduleByID(scheduleID).getEvents();
        return schedule_events;
    }

    /**
     * Gets all public schedules in a list.
     * @return list of public schedules.
     */
    public List<Schedule> getAllPublicSchedule() {
        List<Schedule> public_schedule = new ArrayList<>();
        for (List<Schedule> s_list : schedulesList.values()) {
            for (Schedule s : s_list) {
                if (s.getStatus().equals("public")) {
                    public_schedule.add(s);
                }
            }
        }
        return public_schedule;
    }

    /**
     * Checks whether there exists schedule with the given scheduleID belongs to the user with the given userID.
     * @param userID user's id.
     * @param scheduleID schedule's id.
     * @return true iff this schedule belongs to the user.
     */
    public boolean checkScheduleBelongs(String userID, String scheduleID){
        if (schedulesList.containsKey(userID)){
            for (Schedule s: schedulesList.get(userID)){
                if (s.getScheduleID().equals(scheduleID)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the schedule with the given scheduleID.
     * @param scheduleID schedule's id.
     * @return true iff the schedule exist, but never returns false.
     * @throws ScheduleNotFoundException when the given scheduleID does not exist.
     */
    public Schedule getScheduleByID(String scheduleID) throws ScheduleNotFoundException {
        for (List<Schedule> s_list: schedulesList.values()){
            for (Schedule s: s_list){
                if (s.getScheduleID().equals(scheduleID)){
                    return s;
                }
            }
        }
        throw new ScheduleNotFoundException("This schedule does not exist");
    }

    /**
     * Checks if the user input date or time format is correct. If it is correct, return date in Date.
     * @param date user input date or time.
     * @param dateFormat correct date format.
     * @throws ParseException when the time format is incorrect.
     * @return return date in Date if format is correct.
     */
    // reference: https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
    private Date isDateValid(String date, String dateFormat) throws ParseException {
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setLenient(false);
        df.parse(date);
        return new SimpleDateFormat(dateFormat).parse(date);
    }

    /**
     * Getter of DeletedEvents of a specific schedule
     * @param scheduleID schedule id
     * @return hashmap that is DeletedEvents, with key as event name
     * @throws ScheduleNotFoundException id is not valid.
     */
    public HashMap<String, List<Date>> getDeletedEvents(String scheduleID) throws ScheduleNotFoundException {
        Schedule s = getScheduleByID(scheduleID);
        return s.getDeletedEvents();
    }

    /**
     * Add a most recent deleted event to deletedEvents
     * @param scheduleID schedule id
     * @param eventName deleted event's name
     * @param newDeleted deleted event's date
     * @throws ScheduleNotFoundException id not valid.
     */
    public void addDeletedEvents(String scheduleID, String eventName, List<Date> newDeleted)
            throws ScheduleNotFoundException {
        HashMap<String, List<Date>> deleted = getDeletedEvents(scheduleID);
        deleted.put(eventName, newDeleted);
        Schedule s = getScheduleByID(scheduleID);
        s.setDeletedEvents(deleted);
    }

    /**
     * remove all events with eventName from deletedEvents
     * @param scheduleID schedule's id
     * @param eventName event name
     * @throws ScheduleNotFoundException id not valid
     */
    public void removeDeletedEvents(String scheduleID, String eventName)
            throws ScheduleNotFoundException {
        HashMap<String, List<Date>> deleted = getDeletedEvents(scheduleID);
        deleted.remove(eventName);
        Schedule s = getScheduleByID(scheduleID);
        s.setDeletedEvents(deleted);
    }

    /**
     * Clear deletedEvents
     */
    public void clearDeletedEvents() {
        HashMap<String, List<Date>> clear = new HashMap<>();
        for (List<Schedule> ls : schedulesList.values()) {
            for (Schedule s : ls) {
                s.setDeletedEvents(clear);
            }
        }
    }

    /**
     * share a specific schedule to all friends
     * @param scheduleId schedule id
     * @param um UserManager
     * @throws ScheduleNotFoundException id not valid
     */
    public void shareScheduleToFriends(String scheduleId, UserManager um) throws ScheduleNotFoundException {
        String userID = getScheduleByID(scheduleId).getAuthor();
        ArrayList<String> friendsList = um.getFriendsId(userID);
        for(String friID: friendsList){
            if(schedulesFriend.containsKey(friID)){
                schedulesFriend.get(friID).add(getScheduleByID(scheduleId));
            }
            else{
                ArrayList<Schedule> scheduleList = new ArrayList<>();
                scheduleList.add(getScheduleByID(scheduleId));
                schedulesFriend.put(friID, scheduleList);
            }
        }
    }
}
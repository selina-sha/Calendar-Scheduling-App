package Entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * An abstract class that has subclasses which are DailySchedule and MonthlySchedule.
 * It has a unique id, type (Daily, Weekly or Monthly) and status (public or private or friend-only).
 * events: a hashmap with key as name, values as list of list of start time and end time， which is the format
 * "yyyy MM dd HH:mm“.
 * scheduleName: schedule's name.
 * scheduleDate: schedule's date. If this is a daily schedule, then scheduleDate is the exact date of this
 * schedule. e.g. yyyy mm dd. If this is a weekly schedule, then scheduleDate is the month and year. e.g. yyyy mm dd.
 * If this is a monthly schedule, then scheduleDate is the month and year. e.g. yyyy mm
 * author: user's id
 * status: either public, private or friend-only.
 * scheduleID: unique id.
 * type: type of schedule, either "Monthly", "Daily" or "Weekly"
 * deletedEvents: a hashmap that store all deleted events, key is schedule id, values are events.
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @version 1.8.0
 */
public abstract class Schedule implements Serializable {
    protected String scheduleID;
    protected HashMap<String, List<List<Date>>> events;
    protected String scheduleName;
    protected String scheduleDate;
    protected String author;
    protected String status;
    private final String type = "Default";
    protected HashMap<String, List<Date>> deletedEvents = new HashMap<>();

    /**
     * @param scheduleName schedule's name
     * @param scheduleDate schedule's date
     * @param author id of user who creates this schedule
     * @param status schedule's status (public， private or friend-only).
     */
    public Schedule(String scheduleName, String scheduleDate, String author, String status){
        this.events = new HashMap<>();
        this.scheduleName = scheduleName;
        this.scheduleDate = scheduleDate;
        this.author = author;
        this.status = status;
        this.scheduleID = UUID.randomUUID().toString();
    }

    /**
     * Returns an HashMap called events of the schedule.
     * It is a getter method of events.
     * @return events of schedule
     */
    public HashMap<String, List<List<Date>>> getEvents() {
        return events;
    }

    /**
     * Gets the schedule date.
     * It is a getter method of scheduleDate.
     * @return scheduleDate
     */
    public String getScheduleDate() {
        return scheduleDate;
    }

    /**
     * Gets author's id.
     * Getter of author.
     * @return user's id
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets status of schedule.
     * Getter of status.
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets schedule's id.
     * Getter of scheduleID.
     * @return schedule's id
     */
    public String getScheduleID() {
        return scheduleID;
    }

    /**
     * Sets status, if input is something other than "Public", "Private", or "friend-only", then nothing will happen.
     * Setter of status.
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets events.
     * Setter of events.
     * @param events the new events HashMap
     */
    public void setEvents(HashMap<String, List<List<Date>>> events) {
        this.events = events;
    }

    /**
     * check if a event's start time and end time format is correct.
     * It is an abstract method, we will overwrite it in the subclasses.
     * @param startTimeString start time of event
     * @param endTimeString end time of event
     * @return true if date format is correct, false is never returned.
     * @throws ParseException exception occur when date format is incorrect
     */
    public abstract boolean checkDateFormat(String startTimeString, String endTimeString) throws ParseException;

    /**
     * Gets type of schedule.
     * Getter of type.
     * @return schedule's type
     */
    public String getType() {
        return type;
    }

    /**
     * Return schedule's info, including scheduleID, author, type, scheduleName, scheduleDate.
     * @return schedule's info in a string.
     */
    public abstract String toString();

    /** Getter of deletedEvents
     * @return deletedEvents
     */
    public HashMap<String, List<Date>> getDeletedEvents() {
        return deletedEvents;
    }

    /**
     * Setter of deletedEvents
     * @param deletedEvents going to be set.
     */
    public void setDeletedEvents(HashMap<String, List<Date>> deletedEvents) {
        this.deletedEvents = deletedEvents;
    }
}
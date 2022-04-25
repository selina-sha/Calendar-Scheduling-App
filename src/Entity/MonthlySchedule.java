package Entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * An subclass of the parent class Schedule.
 * It has a unique id, type (Monthly) and status (public or private).
 * events: a hashmap with key as name, and value is in the format of list of list of start time("yyyy MM dd HH:mm“) and end time("yyyy MM dd HH:mm“).
 * scheduleName: schedule's name.
 * scheduleDate: schedule's date, in format of 'yyyy MM'
 * author: user's id.
 * status: either public or private.
 * scheduleID: unique id.
 * type: type of schedule, either "Monthly", "Daily" or "Weekly"
 * deletedEvents: a hashmap that store all deleted events, key is schedule id, values are events.
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @version 1.8.0
 */
public class MonthlySchedule extends Schedule implements Serializable {
    private final String type = "Monthly";

    /**
     * Inherits the method of the super class Schedule.
     * @param scheduleName schedule's name
     * @param scheduleDate schedule's date in format (yyyy MM)
     * @param author id of user who creates this schedule
     * @param status schedule's status, either public or private.
     */
    public MonthlySchedule(String scheduleName, String scheduleDate, String author, String status) {
            super(scheduleName, scheduleDate, author, status);
        }

    /**
     * Getter of type
     * @return schedule's type
     */
    public String getType() {
        return type;
    }

    /**
     * check if a event's start time and end time format is correct(dd HH:mm).
     * @param startTimeString start time of event
     * @param endTimeString end time of event
     * @return true if date format is correct, false is never returned.
     * @throws ParseException exception occur when date format is incorrect
     */
    public boolean checkDateFormat(String startTimeString, String endTimeString)throws ParseException {
        new SimpleDateFormat("dd HH:mm").parse(startTimeString);
        new SimpleDateFormat("dd HH:mm").parse(endTimeString);
        return true;
    }

    /**
     * print schedule
     * @return schedule in a string
     */
    public String toString() {
        return String.format("ID: %s, Owner: %s, Type: %s, Name: %s, DateRange: %s, Status: %s\n",
                scheduleID, author, type, scheduleName, scheduleDate, status);
    }

}

package UseCase;

import Entity.DailySchedule;
import Entity.MonthlySchedule;
import Entity.Schedule;
import Entity.WeeklySchedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A class that is used to generate different types of schedules upon request.
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @version 1.8.0
 */
public class ScheduleFactory {

    /**
     * delete a schedule with given scheduleID and userID, return true if delete successfully, false otherwise.
     * @param type of the template selected (Daily, Weekly, Monthly).
     * @param scheduleDate in the format yyyy MM for MonthlySchedule, yyyy MM dd for DailySchedule.
     * and yyyy MM dd for WeeklySchedule. The scheduleDate for WeeklySchedule is the first day of the selected week.
     * @param scheduleName is the name of this schedule.
     * @param author is the userId of the user who created this schedule.
     * @param status is the status of this schedule. Allowed status are 'public', 'private' and 'friend-only'.
     * @return the schedule generated according to the parameters.
     */
    public Schedule getSchedule(String type, String scheduleDate, String scheduleName, String author, String status) throws ParseException {

        if(type == null){
            return null;
        }

        if(type.equalsIgnoreCase("DailyTemplate")){
            String dateFormat = "yyyy MM dd";
            isDateValid(scheduleDate, dateFormat);
            return new DailySchedule(scheduleName, scheduleDate, author, status);
        }

        else if(type.equalsIgnoreCase("MonthlyTemplate")){
            String dateFormat = "yyyy MM";
            isDateValid(scheduleDate, dateFormat);
            return new MonthlySchedule(scheduleName, scheduleDate, author, status);
        }

        else if(type.equalsIgnoreCase("WeeklyTemplate")){
            String dateFormat = "yyyy MM dd";
            isDateValid(scheduleDate, dateFormat);
            return new WeeklySchedule(scheduleName, scheduleDate, author, status);

        } else {
            System.out.println ("A " + type.toLowerCase() + " is an undefined template for this program.");
            return null;
        }
    }

    /**
     * Checks if the user input date or time format is correct, if not throw ParseException.
     * @param date user input date or time.
     * @param dateFormat correct date format.
     * @throws ParseException when the time format is incorrect.
     */
    private void isDateValid(String date, String dateFormat) throws ParseException {
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setLenient(false);
        df.parse(date);
    }
}



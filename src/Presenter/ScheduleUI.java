package Presenter;

import Controller.Facade;
import Entity.Schedule;
import UseCase.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * This class is a controller for schedule UI, which is to interact with human-user by showing instructions
 * on console and getting user-input.
 *
 * facade: a Facade object
 * sc: a Scanner object to read input
 * userType: an attribute to store user's type
 * userId: an attribute to store user's id
 * templateId: an attribute to store template id
 *
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @author Siqing Xu
 * @author Qing Lyu
 * @author Zhen Cheng
 * @author Christine Chen
 * @author Chuanrun Zhang
 */
public class ScheduleUI {
    private Facade facade;
    private Scanner sc;
    private String userType = "";
    private String userId = "";
    private String templateId;

    /**
     * A constructor for ScheduleUI object
     * @param facade a Facade object
     * @param scanner scanner to receive input from users
     */
    public ScheduleUI(Facade facade, Scanner scanner) {
        this.facade = facade;
        sc = scanner;
    }

    /**
     * Setter for user id
     * @param userId new id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Setter for user type
     * @param userType new type
     */
    public void setUserType(String userType){
        this.userType = userType;
    }

    /**
     * A method for all types users to look at schedules, create/delete/edit schedules or undo deleted schedule.
     * User can enter 1 to 7 to select what they want to do.
     * Enter 8 to return to main menu.
     * Other options are not accepted. User is allowed to type the option again till it is valid.
     * @param templateId template id that used to create schedule, must be selected first
     * @throws IOException when user enter 8 and the file to save schedule has error
     */
    public void runSchedule(String templateId) throws IOException {
        this.templateId = templateId;
        boolean bool = true;
        while (bool) {
            System.out.println("\nEnter '1' to create schedule " +
                    "(please make sure a template has been selected before),\n" +
                    "'2' to display public schedules,\n" +
                    "'3' to display my schedules,\n" +
                    "'4' to display schedules shared with me\n" +
                    "'5' to delete schedule, \n" +
                    "'6' to see inside or edit a schedule(including others' schedule), \n" +
                    "'7' to undo previously deleted schedule, \n" +
                    "(please notice that you are not allowed to undo previously deleted schedule once you exit to the main menu), \n" +
                    "'8' to return to the main menu: ");
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    if (!(runScheduleOption1())) {
                        bool = false;
                    }break;
                case "2":
                    facade.ss.displayPublicSchedule(); break;
                case "3":
                    try {
                        facade.ss.displayUserSchedule(userId);
                    } catch (ScheduleNotFoundException e) {
                        System.out.println(e.getMessage());
                    } break;
                case "4":
                    if (userType.equals("admin")) {
                        System.out.println("There is no schedule shared with an admin user!");
                    } else {
                        facade.ss.displayFriendsSchedule(userId);
                    } break;
                case "5":
                    runScheduleOption5(); break;
                case "6":
                    runScheduleOption6(); break;
                case "7":
                    facade.ss.recoverSchedule(); break;
                case "8":
                    facade.ss.clearDeletedSchedulesAndEvents();
                    if (!(userType.equals("trial"))) {
                        facade.saveSchedule(userId);
                        facade.exitTemp();
                    }
                    bool = false; break;
                default:
                    System.out.println("The option you entered does not exist! Please try again."); break;
            }
        }
    }

    /**
     * Helper method of runSchedule, used when user choose to create a schedule.
     * Let user enter info about the schedule they want to create, save created schedule which is not created by trial.
     * @return true if created successfully.
     */
    private boolean runScheduleOption1() {
        System.out.println("Please enter schedule name: \n" +
                "(please make sure you have selected a template in 'Select a Template' module.)");
        String scheduleName = sc.nextLine();
        String status;
        if (userType.equals("public")) {
            status = "public";
        } else if (userType.equals("private")) {
            status = "private";
        } else {
            status = facade.ss.checkStatusInputValid(userType, sc);
        }
        while (true) {
            try {
                String scheduleDate;
                facade.ss.enterDatebyTemplateType(templateId);
                scheduleDate = sc.nextLine();
                String scheduleId = facade.ss.createSchedule(userId, status, scheduleName, scheduleDate, templateId);
                System.out.println("New schedule is created.");
                if (status.equals("friend-only")) {
                    facade.sm.shareScheduleToFriends(scheduleId, facade.um);
                }
                if (!(userType.equals("trial"))) {
                    facade.saveSchedule(userId);
                    facade.exitTemp();
                }
                break;
            } catch (ParseException e) {
                System.out.println("Schedule date or month is invalid. \n" +
                        "Enter '-1' to exit, or anything else to input new schedule date.");
                String stopCreation = sc.nextLine();
                if (stopCreation.equals("-1")){
                    System.out.println("No schedule is created and return to last menu.");
                    break;
                }
            } catch (TemplateNotFoundException e) {
                System.out.println("You have not selected a template. Please select one first. You can choose " +
                        "'play with template' to select one.");
                return false;
            } catch (ScheduleNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Helper method of runSchedule, used when user(account not freeze) want to delete a schedule
     * Display all schedules for admin user, display their own schedules to other users. Then user can choose one
     */
    private void runScheduleOption5() {
        while (true) {
            String scheduleID;
            Schedule deleteS;
            HashMap<Integer, String> indexToId = new HashMap<>();
            if (userType.equals("admin")) {
                try {
                    indexToId = facade.ss.GetScheduleRecords();
                } catch (ScheduleNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            } else {
                try {
                    facade.ss.displayUserSchedule(userId);
                } catch (ScheduleNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
            System.out.println("Please input the number in front of the schedule you want to delete or '-1' to exit: ");
            try{
                int choice = Integer.parseInt(sc.nextLine());
                if (choice != -1) {
                    if (userType.equals("admin")) {
                        scheduleID = indexToId.get(choice);
                        deleteS = facade.sm.getScheduleByID(scheduleID);
                    } else {
                        deleteS = facade.sm.getSchedulesList().get(userId).get(choice - 1);
                        scheduleID = deleteS.getScheduleID();
                    }
                    String tempID = facade.sm.getScheduleTempMap().get(scheduleID);
                    if (userType.equals("admin") || facade.sm.checkScheduleBelongs(userId, scheduleID)) {
                        if (facade.um.checkUnfreeze().contains(facade.sm.getScheduleByID(scheduleID).getAuthor()) &&
                                facade.ss.deleteSchedule(scheduleID)) {
                            facade.ss.addDeletedScheduleAndTemp(deleteS, tempID);
                            System.out.println("Delete successfully.");
                        } else {
                            System.out.println("This user is frozen, you cannot delete their schedules.");
                        }
                    }
                }
                break;
            } catch (NumberFormatException e){
                System.out.println("The input you entered is not an integer. Please try again.");
            } catch (IndexOutOfBoundsException e){
                System.out.println("There is no such schedule number.");
            } catch (ScheduleNotFoundException e) {
                System.out.println("Schedule is not found.");
            }
        }
    }

    /**
     * Helper method of runSchedule, allow user to edit a schedule
     */
    private void runScheduleOption6() {
        label:
        while (true) {
            System.out.println("Enter '1' to see or edit your schedules \n" + "Enter '2' to see or edit others' schedules \n" +
                    "Enter '-1' to exit \n");
            String option1 = sc.nextLine();
            switch (option1) {
                case "1":
                    editYourSchedule(); break;
                case "2":
                    editOthersSchedule(); break;
                case "-1":
                    break label;
                default:
                    System.out.println("The option you entered does not exist! Please try again.");
            }
        }
    }

    /**
     * Helper method used when user want to edit their own schedules.
     * records which schedule the user want to edit.
     */
    private void editYourSchedule() {
        while (true) {
            Schedule s;
            if (facade.ss.chooseMySchedule(userId)) break;
            try{
                int choice = Integer.parseInt(sc.nextLine());
                s = facade.sm.getSchedulesList().get(userId).get(choice-1);
                editSchedule(s.getScheduleID());
                if (!(userType.equals("trial"))){
                    facade.saveSchedule(userId);
                    facade.exitTemp();
                }
                break;
            } catch (NumberFormatException e){
                System.out.println("The input you entered is not an integer. Please try again.");
            } catch (IndexOutOfBoundsException e){
                System.out.println("There is no such schedule number.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Helper method used when user want to edit others' schedules.
     * Display all schedules that the user can edit and record their choice.
     */
    private void editOthersSchedule() {
        while (true) {
            if (userType.equals("admin")) {
                try {
                    HashMap<Integer, String> indexToId = facade.ss.GetScheduleRecords();
                    System.out.println("The above are the all schedules that all users have.\n" +
                            "Please input the number in front of the schedule you want to see or edit: ");
                    int choice = Integer.parseInt(sc.nextLine());
                    Schedule s = facade.sm.getScheduleByID(indexToId.get(choice));
                    if (ifUnfreeze(s)) break;
                } catch (ScheduleNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("The input you entered is not an integer. Please try again.");
                } catch (IndexOutOfBoundsException e){
                    System.out.println("There is no such schedule number.");
                }
            } else {
                facade.ss.displayFriendsSchedule(userId);
                System.out.println("The above are the schedules shared with you. \n");
                System.out.println("You can input one schedule ID above (please input ID, not the number ahead), \n" +
                        "or you can input a private schedule ID if your friend told you through message before: ");
                String choice = sc.nextLine();
                try {
                    Schedule s = facade.sm.getScheduleByID(choice);
                    if (ifUnfreeze(s)) break;
                } catch (ScheduleNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
        }
    }

    /**
     * Check if a user is freeze
     * @param s schedule that belong to the user
     * @return true if not freeze, false if freeze
     */
    private boolean ifUnfreeze(Schedule s) {
        if (facade.um.checkUnfreeze().contains(s.getAuthor())) {
            try {
                editSchedule(s.getScheduleID());
                if (!(userType.equals("trial"))) {
                    facade.saveSchedule(userId);
                    facade.exitTemp();
                    return true;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("This user is frozen, you cannot interact with their schedules." +
                    "\n Enter '-1' to exit, or type anything else to continue choosing a schedule");
            String choice = sc.nextLine();
            return choice.equals("-1");
        }
        return false;
    }

    /**
     * A helper method used when user choose to see inside or edit a schedule
     * allows a user to edit selected schedule, such as create/delete/(undo deleted) events,
     * update/undo schedule's status, display all events.
     * User can enter 1 to 6 to select what they want to do with this schedule
     * Enter 7 to exit this schedule.
     * Other options are not accepted. User is allowed to type the option again till it is valid.
     */
    public void editSchedule(String scheduleId) throws IOException {
        label:
        while (true) {
            System.out.println("\nEnter '1' to create a new event, or add another time for an already added event,\n" +
                    "'2' to delete an existing event,\n" +
                    "'3' to update status of this schedule,\n" +
                    "'4' to display all the events in this schedule, \n" +
                    "'5' to undo changing status of this schedule, \n" +
                    "(please notice that you are not allowed to undo changing status of this schedule once you exit this schedule), \n" +
                    "'6' to undo deleting a previous event, \n" +
                    "'7' to exit this schedule: ");
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    facade.ss.generateNewEvent(scheduleId, sc); break;
                case "2":
                    System.out.println("Please input the event name of the event you want to delete: ");
                    String eventName = sc.nextLine();
                    while (true) {
                        if (editScheduleOption2(scheduleId, eventName))
                            break;
                    } break;
                case "3":
                    editScheduleOption3(scheduleId); break;
                case "4":
                    try {
                        facade.ss.displayScheduleEvents(scheduleId);
                    } catch (ScheduleNotFoundException e) {
                        System.out.println(e.getMessage());
                    } break;
                case "5":
                    try {
                        facade.ss.restoreStatus(scheduleId, facade.um);
                    } catch (SameStatusException e) {
                        System.out.println(e.getMessage());
                    } break;
                case "6":
                    try {
                        facade.ss.recoverEvent(scheduleId);
                    } catch (ScheduleNotFoundException | UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    } break;
                case "7":
                    facade.ss.clearPreviousStatus();
                    if (!(userType.equals("trial"))) {
                        facade.saveSchedule(userId);
                        facade.exitTemp();
                    } break label;
                default:
                    System.out.println("The option you entered does not exist! Please try again."); break;
            }
        }
    }

    /**
     * Helper method used when user want to delete a event
     * @param scheduleId schedule id
     * @param eventName event name
     * @return true if exit deleting event option, false if want to continue
     */
    private boolean editScheduleOption2(String scheduleId, String eventName) {
        try {
            ArrayList<String> startEnd = facade.ss.getStartEndbyType(scheduleId, sc);
            if (!(facade.sm.deleteEvent(scheduleId, eventName, startEnd.get(0), startEnd.get(1)))) {
                System.out.println("The event you want to delete does not exist!");
            } else {
                System.out.println("event successfully deleted");
                facade.ss.addDeletedEvent(scheduleId, eventName);
            }
            return true;

        } catch (ParseException | ScheduleNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Enter '-1' to exit, or type anything else to continue deleting process.");
        String continue_delete = sc.nextLine();
        if (continue_delete.equals("-1")) {
            System.out.println("Fail to delete the event, and return to the last menu.");
            return true;
        }
        return false;
    }

    /**
     * helper method used to allow user edit schedule's status.
     * If new status is friend-only, share this schedule to user's friends.
     * @param scheduleId this schedule's id.
     */
    private void editScheduleOption3(String scheduleId) {
        if (userType.equals("public") || userType.equals("private")){
            System.out.println("You are a " + userType + "user. You cannot change the schedule status.");
        }
        else{
            while (true) {
                if (userType.equals("admin")){
                    System.out.println("Please enter schedule status you want to change to: public or private.\n" +
                            "or enter '-1' to exit");
                    String status = sc.nextLine();
                    if (status.equals("-1")) {
                        break;
                    }
                    if (!(status.equals("public") || status.equals("private"))) {
                        System.out.println("Input invalid.");
                    } else {
                        try {
                            String prevstatus = facade.sm.getScheduleByID(scheduleId).getStatus();
                            facade.sm.changeStatus(scheduleId, status);
                            System.out.println("Status successfully updated to " + status + "!");
                            facade.ss.addPreviousStatus(prevstatus);
                            break;
                        } catch (ScheduleNotFoundException | SameStatusException e){
                            System.out.println(e.getMessage());
                            break;
                        }
                    }
                }
                else {
                    System.out.println("Please enter schedule status you want to change to: public, private or friend-only.\n"
                                    + "Note: 'friend-only' will share this schedule to author's friends. \n" +
                            "or enter '-1' to exit");
                    String status = sc.nextLine();
                    if (status.equals("-1")) {
                        break;
                    }
                    if (!(status.equals("public") || status.equals("private") || status.equals("friend-only"))) {
                        System.out.println("Input invalid.");
                    } else {
                        try {
                            String prevstatus = facade.sm.getScheduleByID(scheduleId).getStatus();
                            facade.sm.changeStatus(scheduleId, status);
                            if (status.equals("friend-only")) {
                                facade.sm.shareScheduleToFriends(scheduleId, facade.um);
                            }
                            System.out.println("Status successfully updated to " + status + "!");
                            facade.ss.addPreviousStatus(prevstatus);
                            break;
                        } catch (ScheduleNotFoundException | SameStatusException e){
                            System.out.println(e.getMessage());
                            break;
                        }
                    }
                }
            }
        }
    }
}

package Controller;

import Entity.Schedule;
import Gateway.FileReadWriter;
import Gateway.ReadFileListener;
import Gateway.SaveFileListener;
import UseCase.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Facade is a class that contains references to each individual class including Template, Schedule and User related
 * entities, use cases, controllers and Gateways. Facade has roughly the same responsibilities as the original class.
 * It also does the initial setup for two Observables FileReadWriter and MessageSender.
 *
 * ts: TemplateSystem
 * tm: TemplateManager
 * sm: ScheduleManager
 * ss: ScheduleSystem
 * um: UserManager
 * ls: LoginSystem
 * frw: FileReadWriter
 * mm: MessageManager
 * ms: MessageSender
 *
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @author Siqing Xu
 * @author Zhen Cheng
 * @author Qing Lyu
 *
 */
public class Facade {
    public TemplateSystem ts;
    public TemplateManager tm;
    public ScheduleManager sm;
    public ScheduleSystem ss;
    public UserManager um;
    public LoginSystem ls;
    public FileReadWriter frw;
    public MessageManager mm;
    public MessageSender ms;

    /**
     * The constructor of facade that initialize the Template, Schedule, User related entity, use case and controller.
     * Calls setFileObservable and setMsgObservable to set up for these two Observable.
     * <p>
     * ts: TemplateSystem
     * tm: TemplateManager
     * tr: TempReadWriter
     * sr: ScheduleReadWriter
     * sm: ScheduleManager
     * ss: ScheduleSystem
     * um: UserManager
     * ls: LoginSystem
     * frw: FileReadWriter
     * mm: MessageManager
     * ms: MessageSender
     */
    public Facade() throws IOException {
        um = new UserManager();
        tm = new TemplateManager();
        sm = new ScheduleManager();
        mm = new MessageManager(um);
        ts = new TemplateSystem(tm);
        ss = new ScheduleSystem(sm, tm);
        ls = new LoginSystem(um);
        setFileObservable();
        setMsgObservable();
    }

    /**
     * Do the initial setup for FileReadWriter.
     * Creates operations(fileType) for FileReadWriter and initializes it, and subscribes objects for ReadFile and SaveFile
     * in frw's events with corresponding operations.
     */
    private void setFileObservable(){
        ArrayList<String> operations = new ArrayList<>();
        operations.add("readFile");
        operations.add("saveScheduleData");
        operations.add("saveTemplateData");
        operations.add("saveUserInfo");
        operations.add("saveSuspend");
        operations.add("saveFreeze");
        operations.add("saveLoginTime");
        operations.add("saveTempInfo");
        operations.add("saveWelMsg");
        operations.add("saveTempPwd");
        operations.add("saveCommonInbox");
        operations.add("saveChatHistory");
        frw = new FileReadWriter(operations);
        frw.events.subscribe("readFile", new ReadFileListener(tm, sm, mm, ls));
        SaveFileListener sf = new SaveFileListener(tm, sm, mm, ls);
        frw.events.subscribe("saveScheduleData", sf);
        frw.events.subscribe("saveTemplateData", sf);
        frw.events.subscribe("saveUserInfo", sf);
        frw.events.subscribe("saveSuspend", sf);
        frw.events.subscribe("saveFreeze", sf);
        frw.events.subscribe("saveLoginTime", sf);
        frw.events.subscribe("saveTempInfo", sf);
        frw.events.subscribe("saveWelMsg", sf);
        frw.events.subscribe("saveCommonInbox", sf);
        frw.events.subscribe("saveChatHistory", sf);
    }

    /**
     * Do the initial setup for MessageSender.
     * Creates operations(fileType) for MessageSender and initializes it, and subscribes an object for SendMsgListener
     * in ms's events with corresponding operations.
     */
    private void setMsgObservable(){
        ArrayList<String> operations = new ArrayList<>();
		operations.add("regularToAdmin");
		operations.add("regularToOther");
		operations.add("adminToAll");
		operations.add("adminToOther");
		ms = new MessageSender(operations);
		SendMsgListener sml = new SendMsgListener(mm);
		ms.events.subscribe("regularToAdmin", sml);
		ms.events.subscribe("regularToOther", sml);
		ms.events.subscribe("adminToAll", sml);
        ms.events.subscribe("adminToOther", sml);
    }

    /**
     * A getter for ScheduleManager sm
     * @return sm
     */
    public ScheduleManager getSm() {
        return sm;
    }

    /**
     * A getter for MessageManager mm
     * @return mm
     */
    public MessageManager getMm() { return this.mm; }

    /**
     * crete a template with the type given.
     *
     * @param TemplateType the type of a template (monthly or daily)
     */
    public void createTemp(String TemplateType) {
        ts.create(TemplateType);
    }


    /**
     * Get template records (the id, type, time limitations) in a string representation and returns a Hashmap
     * from GetTemplateRecords method.
     *
     */
    public HashMap<Integer, String> getTempData() {
        return ts.GetTemplateRecords();
    }


    /**
     * save the creation or modification of the template to the temp file.
     */
    public void exitTemp() throws IOException {
        frw.saveFile("saveTemplateData");
    }

    /**
     * save the creation or modification of the schedule to the schedule file.
     * @param id: the schedule id
     */

    public void saveSchedule(String id) throws IOException {
        HashMap<String, List<Schedule>> scheduleList = sm.getSchedulesList();
        HashMap<String, String> scheduleTempMap = sm.getScheduleTempMap();
        HashMap<String, List<Schedule>> schedulesFriend = sm.getSchedulesFriend();
        if (um.getUserType(id).equals("trial")) {
            if (scheduleList.containsKey(id)) {
                List<Schedule> userSchedules = scheduleList.get(id);
                scheduleList.remove(id);
                schedulesFriend.remove(id);
                for (Schedule s : userSchedules) {
                    scheduleTempMap.remove(s.getScheduleID());
                }
            }

            sm.setSchedulesList(scheduleList);
            sm.setSchedulesFriend(schedulesFriend);
            sm.setScheduleTempMap(scheduleTempMap);
        }
        frw.saveFile("saveScheduleData");
    }

    /**
     * a method to create a template (MonthlyTemplate, DailyTemplate or WeeklyTemplate) for admin users.
     * @param sc a scanner
     */

    public void createTemp1(Scanner sc){
        System.out.println("Please enter template type: MonthlyTemplate, DailyTemplate or WeeklyTemplate");
        String tempType = sc.nextLine();
        if (tempType.equals("MonthlyTemplate") || tempType.equals("DailyTemplate")
                || tempType.equals("WeeklyTemplate")) {
            createTemp(tempType);
            System.out.println("A default Template is created successfully!");
        }else
            System.out.println("The template type you entered is incorrect! Please try again.");
        }
    }


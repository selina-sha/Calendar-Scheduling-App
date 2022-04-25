package Presenter;

import Controller.Facade;
import Entity.Template;
import UseCase.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class is a controller for text UI, which is to interact with human-user by showing instructions
 * on console and getting user-input.
 *
 * ls: a LoginSystem object
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
public class TemplateUI {
    private Facade facade;
    private Scanner sc;

    /**
     * A constructor for TemplateUI object with parameters facade and scanner passed in.
     * It initializes the Scanner object sc.
     *
     * @param facade a Facade object
     */
    public TemplateUI(Facade facade, Scanner scanner) {
        this.facade = facade;
        sc = scanner;
    }

    /**
     * A method for admin users which is used to create default templates, or see all templates and modify them.
     * Enter '1' and then enter template type to create a default template.
     * Enter '2' to see all template, and then enter the id chosen or enter '0' to exit. Enter id which doesn't
     * exist will throw TemplateNotFoundException. If id has an invalid format it will throw NumberFormatException.
     * They will both be caught and corresponding error messages will be printed on console.
     * After choosing a template, enter number to set the value
     * of attributes stored in the chosen template.
     * Enter '-1' to delete the chosen template.
     * Enter '3' to exit.(return to the main menu)
     * Enter any other option will go back to the start of the while loop.
     * option: the number client entered.
     */
    public void editTemplate(ScheduleUI scheduleUI) throws IOException {
        while (true){
            System.out.println("Enter '1' to create a default template, \n" +
                    "'2' to see all the template and/or modify the chosen one, \n" +
                    "'3' to select a template for schedule, \n" +
                    "'4' to return to the main menu: ");
            String option = sc.nextLine();
            if(option.equals("1")){
                facade.createTemp1(sc);
            }
            else if (option.equals("2")) {
                HashMap<Integer, String> indexToId = facade.getTempData();
                System.out.println("Please input the number in front of the template you want to modify, \n" +
                        "enter '0' to return to the main menu, \n" +
                        "enter '-1' to delete template: ");
                String option2 = sc.nextLine();
                if (option2.equals("-1")) {
                    while (true) {
                        System.out.println("Please input the number in front of the template you want to delete, \n" +
                                "enter '0' to return to the main menu: ");
                        option2 = sc.nextLine();
                        if (option2.equals("0")){
                            facade.exitTemp();
                            break;
                        }
                        try {
                            int index = Integer.parseInt(option2);
                            facade.ts.delete(indexToId.get(index));
                            facade.exitTemp();
                            System.out.println("The template has successfully deleted. Now back to the main menu.");
                            break;
                        } catch (TemplateNotFoundException e) {
                            System.out.println("There is no such template number. Please enter a existed one.");
                        } catch (NumberFormatException e){
                            System.out.println("Your input is not valid! Please enter an existed integer.");
                        }
                    }
                    break;
                } else if (option2.equals("0")) {
                    facade.exitTemp();
                    break;
                } else {
                    try {
                        int index = Integer.parseInt(option2);
                        Template t = facade.tm.getTemplateById(indexToId.get(index));
                        setTemplateLimits(t);
                        facade.exitTemp();
                    } catch (TemplateNotFoundException e) {
                        System.out.println("The template id is not valid. Please try again.");
                    } catch (NumberFormatException e) {
                        System.out.println("Your input is not valid! Please enter an existed integer.");
                    }
                }
                break;
            }
            else if (option.equals("3")){
                selectTemplate(scheduleUI);
                break;
            }
            else if (option.equals("4")){
                facade.exitTemp();
                break;
            }
            else
                System.out.println("The option you entered does not exist! Please try again.");
        }
    }

    /**
     * A helper method for set time limits for a certain template.
     *
     * @param t the template need to set limits.
     *
     * Set the Minimum, Maximum time between events as well as the minimum time of an events.
     * The minimum time can not exceed the maximum time. Otherwise we a valid time limits is required.
     *
     */
    public void setTemplateLimits(Template t) {
        String option2;
        while (true) {
            System.out.println("Please enter the minimum time between events(unit hour): ");
            try {
                option2 = sc.nextLine();
                t.setMinTimeBtwEvents(Double.parseDouble(option2));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Fail to change. Please input a number representing the MinTimeBtwEvents. ");
            }
        }
        while (true) {
            System.out.println("Please enter the minimum time and the maximum time of an event" +
                    "(format in unit hour: 1.0, 30.0): ");
            try {
                String s = sc.nextLine();
                String[] a = s.split(",");
                if (a.length != 2) {
                    System.out.println("Please enter the answer in two numbers, separated by ','. ");
                }
                else {
                    if(Double.parseDouble(a[0]) <= Double.parseDouble(a[1])){
                        t.setMinTimeOfEvent(Double.parseDouble(a[0]));
                        t.setMaxTimeOfEvent(Double.parseDouble(a[1]));
                        System.out.println("You successfully edited the template.");
                        break;
                    }
                    else {
                        System.out.println("Fail to change. Please enter the minimum time which is " +
                                "smaller than or equal to the maximum time.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Fail to change. Please input numbers representing the MinTimeOfEvent and MaxTimeOfEvent. ");
            }
        }
    }

    /**
     * A method for regular/trial users which is used to see all templates and choose one to create their schedules.
     * Enter '1' to see all templates. And then enter the id of the template chosen.
     * catch NumberFormatException when the client enter a non-integer template id.
     * If a valid template id is chosen, calls runSchedule method to show latter instructions.
     * Enter '2' to exit.
     * Enter any other option will go back to the start of the while loop.
     */
    public void selectTemplate(ScheduleUI scheduleUI) throws IOException {
        while (true){
            System.out.println("Enter '1' to see all the template and select one, \n" +
                    "'2' to return to the main menu:");
            String option = sc.nextLine();
            if (option.equals("1")) {
                HashMap<Integer, String> indexToId = facade.getTempData();
                System.out.println("Enter input the number in front of the template you want to choose");
                try {
                    String potential = sc.nextLine();
                    int index = Integer.parseInt(potential);
                    if (facade.tm.checkExistedTemplate(indexToId.get(index))){
                        String templateId = indexToId.get(index);
                        System.out.println("You have chosen template id: " + templateId + ".");
                        scheduleUI.runSchedule(templateId);
                        break;
                    }
                    else {
                        System.out.println("The template id does not exist. Please enter a existed one.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("You need to input a number.");
                }
            }
            else if (option.equals("2")){
                facade.exitTemp();
                break;
            }
            else
                System.out.println("The option you entered does not work! Please try again.");
        }
    }
}
